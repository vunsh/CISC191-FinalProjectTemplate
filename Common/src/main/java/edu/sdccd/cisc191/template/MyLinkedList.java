package edu.sdccd.cisc191.template;

import java.io.Serializable;

/**
 * The {@code MyLinkedList} class implements a singly linked list that can store elements of any type.
 * It supports basic operations such as adding, removing, and retrieving elements, as well as clearing the list.
 *
 * @param <T> the type of elements in this list
 */
public class MyLinkedList<T> implements Serializable {
    private static final long serialVersionUID = 1L; // Add a serialVersionUID for version control

    private Node<T> head;
    private int size;

    /**
     * Constructs an empty {@code MyLinkedList}.
     */
    public MyLinkedList() {
        head = null;
        size = 0;
    }

    /**
     * Clears the linked list, removing all elements.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Adds an element to the end of the linked list.
     *
     * @param data the element to be added
     */
    public void add(T data) {
        Node<T> newNode = new Node<>(data);
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
        size++;
    }

    /**
     * Removes the first occurrence of the specified element from the linked list.
     *
     * @param data the element to be removed
     */
    public void remove(T data) {
        if (head == null) return;

        if (head.data.equals(data)) {
            head = head.next;
            size--;
            return;
        }

        Node<T> current = head;
        while (current.next != null && !current.next.data.equals(data)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
            size--;
        }
    }

    /**
     * Retrieves and removes the first element of the linked list.
     *
     * @return the first element of the linked list, or {@code null} if the list is empty
     */
    public T pollFirst() {
        if (head == null) return null;

        T data = head.data;
        head = head.next;
        size--;
        return data;
    }

    /**
     * Retrieves, but does not remove, the first element of the linked list.
     *
     * @return the first element of the linked list, or {@code null} if the list is empty
     */
    public T peekFirst() {
        return (head != null) ? head.data : null;
    }

    /**
     * Displays all elements in the linked list.
     */
    public void display() {
        Node<T> current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    /**
     * Returns the number of elements in the linked list.
     *
     * @return the size of the linked list
     */
    public int size() {
        return size;
    }

    /**
     * The {@code Node} class represents a node in a singly linked list, storing a reference to its data and the next node.
     *
     * @param <T> the type of element in this node
     */
    private static class Node<T> implements Serializable { // Make Node class serializable
        private static final long serialVersionUID = 1L; // Add serialVersionUID to Node as well

        T data;
        Node<T> next;

        /**
         * Constructs a {@code Node} with the specified data.
         *
         * @param data the element stored in this node
         */
        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }
}
