#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <signal.h>
#include <sys/types.h>
#define MAX 80

// Structs for linked list
typedef struct node{
    char* data;
    struct node* next;
}node_t;

typedef struct linkedlist{
    node_t* head;
}linkedlist_t;

// Declare these here so the signal handler can use them
void FreeLinkedList(linkedlist_t* list);
linkedlist_t* historyList;

// Create a signal handler
void sigint_handler(int sig){

    write(1,"\nTerminating through signal handler\n",35);
    FreeLinkedList(historyList); 
    exit(0);
}

// Functions for special commands
void changeDirectory(char** argumentArray){
    // Make pathArg into a const char* path
    const char* path = (const char*) argumentArray[1];

    // Use chdir()
    chdir(path);

}

void printHistory(linkedlist_t* list){
    printf("Command history: \n");
    node_t* cursor = list->head;
    while(cursor != NULL){
        printf("%s\n", cursor->data);
    cursor = cursor->next;
    }
    exit(0);
   
}

void printHelp(char** unusedArgumentArray) {
    printf("\nList of available custom functions:\n\n");
    printf("cd: Change which directory you're in. Takes in a file path as an argument\n");
    printf("history: Print out a list of previously-input commands. No arguments.\n");
    printf("exit: Exit the shell. No arguments.\n");
    printf("help: Print out a list of available custom functions. No arguments.\n\n");
}

// Functions for linked list implementation

void FreeLinkedList(linkedlist_t* list){
    node_t* cursor;
    node_t* temp;

    if(list->head != NULL){
        cursor = list->head;
    while(cursor != NULL){
        temp = cursor->next;
        free(cursor->data);
        free(cursor);
        cursor = temp;
    }
    free(list);
    }
    else{
        free(list);
    }
}

void AppendToLinkedList(linkedlist_t* list, char* data){
    node_t* newNode = (node_t*)malloc(sizeof(node_t));
    newNode->data = data;
    newNode->next = NULL;
    
    if(list->head == NULL){
        list->head = newNode;
    }
    else{
        node_t* cursor = list->head;
    while(cursor->next != NULL){
        cursor = cursor->next;
    }
    cursor->next = newNode;
    }  
    
}

void printHeader(){
printf("\n");
printf(" .oooooo..o ooooo   ooooo oooooooooooo ooooo        ooooo        \n");
printf("d8P'    `Y8 `888'   `888' `888'     `8 `888'        `888'        \n");
printf("Y88bo.       888     888   888          888          888         \n");
printf(" `\"Y8888o.   888ooooo888   888oooo8     888          888         \n");
printf("     `\"Y88b  888     888   888    \"     888          888         \n");
printf("oo     .d8P  888     888   888       o  888       o  888       o \n");
printf("8\"\"88888P'  o888o   o888o o888ooooood8 o888ooooood8 o888ooooood8 \n\n");

}

// Function to handle pipes
void pipeSitutation(char* linein) {
    // Get two argument arrays, one for each command
    // on either side of the pipe
    char* firstCommand = strtok(linein, "|");
    char* secondCommand = strtok(NULL, "|");
    char* firstArgArray[16];
    char* secondArgArray[16];
    // populate first array
    int i = 0;
    char* pch = strtok(firstCommand, " "); 
    while (pch != NULL) {
            firstArgArray[i] = pch;
            i++;
            if (i > 14) {
                break;
            }
            pch = strtok(NULL, " ");
    }
    firstArgArray[i] = NULL;
    // populate second array
    i = 0;
    pch = strtok(secondCommand, " ");
    while (pch != NULL) {
            secondArgArray[i] = pch;
            i++;
            if (i > 14) {
                break;
            }
            pch = strtok(NULL, " \n");
        }  
    secondArgArray[i] = NULL;
    
    // pipe time
    int fd[2];
    // open pipe
    if (pipe(fd) == -1) {
        printf("An error occured when opening the pipe.\n");
        exit(1);
    } 
    int pid = fork();
    if (pid == 0) {
        // Set STDOUT to the write end of the pipe
        dup2(fd[1], 1); // send STDOUT to pipe
   
        // Close the pipe 
        close(fd[0]);
        close(fd[1]);

        // Execute first command
        execvp(firstArgArray[0], firstArgArray);
        printf("Could not execute command %s.\n", firstArgArray[0]);
        exit(1);
        
    }
    else {

        wait(NULL);

        pid = fork();
        if (pid == 0) {
            // Set STDIN to the read end of the pipe
            dup2(fd[0], 0);
            // Close the pipe
            close(fd[1]);
            close(fd[0]);
            // Execute the second command
            int execReturn = execvp(secondArgArray[0], secondArgArray);
            if (execReturn == -1) {
                printf("Could not execute command %s.\n", secondArgArray[0]);
                exit(1);   
            }
            exit(1);         

        }
        else {
            int status;
            close(fd[0]);
            close(fd[1]);
            waitpid(pid, &status, 0);
        }

    }     

}


int main(){

    alarm(1080);

    // Install our signal handler
    signal(SIGINT, sigint_handler);
   
    // Output the header  
    printHeader();

    printf("You can terminate by pressing Ctrl+C or by using the exit command\n");
    

    // Set up array of functions
    char* functionNameArray[3] = {"cd", "history", "help"};

    // Set up array of function pointers
    // Set up the pointers themselves
    typedef void(*generic_fp)(char**);
    typedef void(*history_fp)(linkedlist_t*);
    void (*cd_ptr)(char**) = &changeDirectory;
    void (*hist_ptr)(linkedlist_t*) = &printHistory;
    void (*help_ptr)(char**) = &printHelp;

    void (*functionArray[3])(char**) = {(generic_fp)cd_ptr, (generic_fp)hist_ptr, 
                                        (generic_fp)help_ptr};

    // Set up array for history
    historyList = (linkedlist_t*)malloc(sizeof(linkedlist_t));
    historyList->head = NULL;

    while(1){
        // Reading in text from the user
        char str[MAX];
        printf("minishell> ");
        fgets(str, MAX, stdin);

        // Check that input is not empty
        if (strcmp(str, "\n") == 0) {
            continue;
        }

        // Remove trailing newline from str
        str[strcspn(str, "\n")] = 0;

        // Check that input is not blank
        char* testString = strdup(str);
        char* testValue = strtok(testString, " ");
        if (testValue == NULL) {
            continue;
        }
        free(testString);

        // Add user input to history log
        AppendToLinkedList(historyList, strdup(str));

        // Check if pipe situation
        if (strchr(str, '|')!= NULL) {
            pipeSitutation(str);
            continue;
        }


        // Populate argument array
        char* myargv[16];
        char* pch = strtok(str, " \n");
        int i = 0;
        while (pch != NULL) {
            myargv[i] = pch;
            i++;
            if (i > 14) {
                break;
            }
            pch = strtok(NULL, " \n");
        } 

        myargv[i] = NULL;

        // Execute the command
        if (fork() == 0) {

            // If argv[0] is cd, just exit because we need to run that
            // in the parent process
            if (strcmp(myargv[0], "cd")==0) {
                exit(0);
            }
            // Additionally, if argv[0] is exit, just because we need to
            // run that in the parent
            if (strcmp(myargv[0], "exit")==0){
                exit(0);
            }

            // For dev purposes, display what is in the 
            // argument array
            //int i;
            //for (i = 0; i < 16; i++) {
            //    if (myargv[i] == NULL) {
            //        break;
            //    }
            //    printf("Array at index %d: ++%s++\n", i, myargv[i]);
            //}
 
            // Check if the intended function is one of the 
            // functions that we had to design specifically for this 
            // minishell (cd, exit, history, help)
            i = 0;
            for (i = 0; i < 3; i++) {
                // If the first argument in our argument array is
                // one of the names in our array of self-defined
                // functions...
                if(strcmp(myargv[0], functionNameArray[i]) == 0) {
                    // need to make an exception here since history takes in 
                    // a linkedlist_t*
                    if(strcmp(myargv[0], "history")==0) {
                        ((history_fp)functionArray[i])(historyList);
                    }
                    // ...run that function
                    functionArray[i](myargv);
                    exit(0);
                }
               
            
            }

            
            int execReturn = execvp(myargv[0], myargv);
            if (execReturn == -1) {
                printf("Command %s not found.\n", myargv[0]);
                exit(1);
            }
            exit(1);
        }
        else {
            wait(NULL);
            if(strcmp(myargv[0], "cd") == 0) {
                functionArray[0](myargv);
            }
            if(strcmp(myargv[0], "exit") == 0) {
                printf("Exiting from shell...\n");
                FreeLinkedList(historyList);
                exit(0);
            }
        }
    }

    return 0;
}

