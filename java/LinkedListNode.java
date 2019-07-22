
public class LinkedListNode<E> {

    public LinkedListNode<E> previous;

    public LinkedListNode<E> next;

    public E object;

    /**
     * This class is further customized for the CoolServlets cache system. It
     * maintains a timestamp of when a Cacheable object was first added to
     * cache. Timestamps are stored as long values and represent the number of
     * milleseconds passed since January 1, 1970 00:00:00.000 GMT.
     * <p>
     * The creation timestamp is used in the case that the cache has a maximum
     * lifetime set. In that case, when [current time] - [creation time] &gt;
     * [max lifetime], the object will be deleted from cache.
     * </p>
     */
    public long timestamp;

    /**
     * Constructs an self-referencing node. This node acts as a start/end
     * sentinel when traversing nodes in a LinkedList.
     */
    public LinkedListNode() {
        previous = next = this;
    }

    /**
     * Constructs a new linked list node.
     *
     * @param object the Object that the node represents.
     * @param next a reference to the next LinkedListNode in the list.
     * @param previous a reference to the previous LinkedListNode in the list.
     */
    public LinkedListNode(E object, LinkedListNode<E> next, LinkedListNode<E> previous) {
        if (next != null && previous != null) {
            this.insert(next, previous);
        }
        this.object = object;
    }

    /**
     * Removes this node from the linked list that it was a part of.
     * 
     * @return This node; next and previous references dropped
     */
    public LinkedListNode<E> remove() {
        previous.next = next;
        next.previous = previous;
        previous = next = null;
        return this;
    }

    /**
     * Inserts this node into the linked list that it will be a part of.
     * 
     * @return This node, updated to reflect previous/next changes
     */
    public LinkedListNode<E> insert(LinkedListNode<E> next, LinkedListNode<E> previous) {
        this.next = next;
        this.previous = previous;
        this.previous.next = this.next.previous = this;
        return this;
    }

    /**
     * Returns a String representation of the linked list node by calling the
     * toString method of the node's object.
     *
     * @return a String representation of the LinkedListNode.
     */
    @Override
    public String toString() {
        return object == null ? "null" : object.toString();
    }

}
