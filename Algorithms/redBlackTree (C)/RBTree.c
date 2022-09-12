#include <stdio.h>
#include <stdlib.h>

#include "RBTree.h"

typedef struct node{
    int data;
    struct node* next;
}node_t;

typedef struct linkedlist{
    node_t* head;
}linkedlist_t;

linkedlist_t* CreateLinkedList(){
    linkedlist_t* myLinkedList = (linkedlist_t*)malloc(sizeof(linkedlist_t));
    myLinkedList->head = NULL;

    return myLinkedList; 
}

void FreeLinkedList(linkedlist_t* list){
    node_t* cursor;
    node_t* temp;

    if(list->head != NULL){
        cursor = list->head;
		while(cursor != NULL){
	    	temp = cursor->next;
	    	free(cursor);
	    	cursor = temp;
		}
		free(list);
    }
    else{
        free(list);
    }
}

void AppendToLinkedList(linkedlist_t* list, int data){
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

void PrintLinkedList(linkedlist_t* list) {
	node_t* cursor = list->head;
	while (cursor != NULL) {
		printf("%d\n", cursor->data);
		cursor = cursor->next;
	}
}

linkedlist_t* getNumbers(const char* file_name) {
	// READ IN NUMBERS FROM TEXT FILE AND
	// ADD THEM TO A LINKED LIST
	linkedlist_t* numList = CreateLinkedList();
    FILE* file = fopen(file_name, "r");
    int i = 0;

    while (!feof (file))
    {  
        fscanf (file, "%d", &i);
		AppendToLinkedList(numList, i);      
    }
    fclose (file);  
	return numList;
	
}



// SOURCE: stackoverflow.com/questions/4600797/read-int-values-from-a-text-file-in-c
int main() {
	
	linkedlist_t* numList = getNumbers("tree2.txt");
	
	// ADD EACH NUMBER IN THE LINKED LIST TO
	// THE RB_TREE
	tree_t* tree = (tree_t*)malloc(sizeof(tree_t));
	tree->size = 0;
	tree->root = NULL;
	node_t* cursor = numList->head;
	
	printf("NOTE before adding node %d\n", cursor->data);

	while(cursor != NULL) {
		tree_node_t* newTreeNode = (tree_node_t*)malloc(sizeof(tree_node_t));
		newTreeNode->color = -1;
		newTreeNode->key = cursor->data;
		newTreeNode->left = NULL;
		newTreeNode->right = NULL;
		newTreeNode->p = NULL;

		printf("NOTE before insert node %d\n", cursor->data);
		RB_INSERT(tree, newTreeNode);
		printf("NOTE after insert %d\n", cursor->data);
		//PRINT_PREORDER(tree);
		PRINT_SORTED(tree);
		cursor = cursor->next;
	}
		
  
	return 0;    
}

