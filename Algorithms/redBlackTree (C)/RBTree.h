#include <stdio.h>
#include <stdlib.h>

// Color will be an integer. Black is 0 and Red is 1.
typedef struct tree_node{
	int color;
	int key;
	struct tree_node* left;
	struct tree_node* right;
	struct tree_node* p;
}tree_node_t;

typedef struct tree{
	tree_node_t* root;
	int size;
}tree_t;

int nodeCompare(tree_node_t* A, tree_node_t* B) {
	if (A == NULL) {
		if (B != NULL) {
			return -1;
		}
	}
	else
		if (B == NULL) {
			return -1;
		}
	printf("NOTE beginning of comparison of %d and %d", A->key, B->key);
	// Compare color and value
	if (A->color != B->color) {
		return -1;
	}
	printf("NOTE after color comparison %d and %d. same color.\n", A->key, B->key);
	if (A->key != B->key){
		return -1;
	}
	printf("NOTE after value comparison %d and %d. same key.\n", A->key, B->key);
	return 1;





	// If one node has null pointer some direction, 
	// then the other must be too
	//if (A->left == NULL) {
	//	if (B->left != NULL) {
	//		return -1;
	//	}
	//}
	//if (B->left == NULL) {
	//	if (A->left != NULL) {
	//		return -1;
	//	}
	//}
	//if (A->right == NULL) {
	//	if (B->right != NULL) {
	//		return -1;	
	//	}
	//}
	//if (B->right == NULL) {
	//	if (A->right != NULL) {
	//		return -1;
	//	}
	//}
	//if (A->p == NULL) {
	//	if (B->p != NULL) {
	//		return -1;
	//	}
	//}
	//if (B->p == NULL) {
	//	if (A->p != NULL) {
	//		return -1;
	//	}
	//}
	//if (A->left != NULL){
	//	if (B->left != NULL) {
	//		if (nodeCompare(A->left, B->left) < 0) {
	//			return -1;
	//		}
	//	}
	//}
	//if (A->right != NULL) {
	//	if (B->right != NULL) {
	//		if (nodeCompare(A->right, B->right) < 0) {
	//			return -1;
	//		}
	//	}
	//}
	//if (A->p != NULL) {
	//	if (B->p != NULL) {
	//		if (nodeCompare(A->p, B->p) < 0) {
	//			return -1;
	//		}
	//	}
	//}
	//return 1;
	
	

}

void LEFT_ROTATE(tree_t* T, tree_node_t* x) {
	tree_node_t* y = x->right;
	x->right = y->left;
	if (y->left != NULL) {
		y->left->p = x;
	}
	y->p = x->p;
	if (x->p == NULL) {
		T->root = y;
	}
	else if (nodeCompare(x, x->p->left) > 0) {
		x->p->left = y;
	}
	else {
		x->p->right = y;
	}
	y->left = x;
	x->p = y;
}

void RIGHT_ROTATE(tree_t* T, tree_node_t* x) {
	tree_node_t* y = x->left;
	x->left = y->right;
	if (y->right != NULL) {
		y->right->p = x;
	}
	y->p = x->p;
	if (x->p == NULL) {
		T->root = y;
	}
	else if (nodeCompare(x, x->p->right) > 0) {
		x->p->right = y;
	}
	else {
		x->p->left = y;
	}
	y->right = x;
	x->p = y;
}




void RB_INSERT_FIXUP(tree_t* T, tree_node_t* z) {
	tree_node_t* y;
	if (z->p == NULL) {
		printf("NOTE z's parent is null, z is %d\n", z->key);
	}
	else {
		printf("NOTE z's (%d) parent is %d\n", z->key, z->p->key);
		printf("NOTE z is color %d\n", z->color);
	}
	while (z->p != NULL && z->p->color == 1) {
		if (nodeCompare(z->p, z->p->p->left) > 0){
			printf("inside fixup case 1 node %d\n", z->key);
			y = z->p->p->right;
			if (y->color == 1) {
				printf("NOTE inside case y is red y is %d\n", y->key);
				z->p->color = 0;
				y->color = 0;
				z->p->p->color = 1;
				z = z->p->p;
				printf("NOTE end case y is red y is %d\n", y->key);
			}
			else {
				printf("NOTE inside case y is black y is %d\n", y->key);
				if (nodeCompare(z, z->p->right) > 0) {
					z = z->p;
					LEFT_ROTATE(T, z);
				}
				z->p->color = 0;
				z->p->p->color = 1;		
				RIGHT_ROTATE(T, z->p->p);
				printf("NOTE end case y is black, y is %d\n", y->key);
			}
		}
		else {
			printf("inside fixup case 2 node %d\n", z->key);
			printf("z's parent is %d\n", z->p->key);
			printf("z's parent's parent is %d\n", z->p->p->key);
			y = z->p->p->left;
			printf("NOTE y is (%d)\n", y->key);
			if (y->color == 1) {
				printf("NOTE case where y (%d) is red\n", y->key);
				z->p->color = 0;
				y->color = 0;
				z->p->p->color = 1;
				z = z->p->p;
			}
			else {
				printf("NOTE case where y (%d) is black\n", y->key);
				if (nodeCompare(z, z->p->left) > 0) {
					z = z->p;
					RIGHT_ROTATE(T, z);
				}
				z->p->color = 0;
				z->p->p->color = 1;
				LEFT_ROTATE(T, z->p->p);
				printf("NOTE end case y is red, y is %d\n", y->key);
			}
		}	
	}
	T->root->color = 0;
}

int RB_INSERT(tree_t* T, tree_node_t* z){
	tree_node_t* y = NULL;
	tree_node_t* x = T->root;
	printf("NOTE beginning of insert node %d\n", z->key);
	if (T->size == 0) {
		printf("inside empty tree\n");
		z->color = 0;
		T->root = z;
		T->size = T->size + 1;
		return 0;
	}
	while (x != NULL) {
		y = x;
		if (z->key < x->key) {
			x = x->left;
		}
		else {
			x = x->right;
		}
	}
	z->p = y;
	if (y == NULL) {
		T->root = z;
	}
	else if (z->key < y->key) {
		y->left = z;
	}
	else {
		y->right = z;
	}
	z->left = NULL;
	z->right = NULL;
	z->color = 1;
	printf("NOTE before fix up with node %d\n", z->key);
	RB_INSERT_FIXUP(T, z);
	T->size = T->size + 1;
	return 0;
		
}

void PRINT_SORTED_REC(tree_node_t* x) {
	if (x != NULL) {
		PRINT_SORTED_REC(x->left);
		printf("%d with color %d\n", x->key, x->color);
		PRINT_SORTED_REC(x->right);
	}
}

void PRINT_SORTED(tree_t* T) {
	printf("PRINTING INORDER\n");
	if (T->root != NULL) {
		PRINT_SORTED_REC(T->root);
	}
}

void PRINT_PREORDER_REC(tree_node_t* x) {
	if (x != NULL) {
		printf("%d with color %d\n", x->key, x->color);
		PRINT_PREORDER_REC(x->left);
		PRINT_PREORDER_REC(x->right);
	}
}

void PRINT_PREORDER(tree_t* T) {
	printf("PRINTING PREORDER\n");
	if (T->root != NULL) {
		PRINT_PREORDER_REC(T->root);
	}
}



tree_node_t* SEARCH_REC(tree_node_t* x, int k) {
	if (x == NULL || k == x->key) {
		return x;
	}
	if (k < x->key) {
		return SEARCH_REC(x->left, k);
	}
	else {
		return SEARCH_REC(x->right, k);
	}
}

void SEARCH(tree_t* T, int k) {
	if (T->root != NULL) {
		SEARCH_REC(T->root, k);
	}
}
