public class LinkList<T> {
	private static class Node<T>{
		Node<T> next;
		T data;
		Node(Node<T> next, T data) {
			this.next = next;
			this.data = data;
		}
		@Override
		public String toString() {
			if(data == null) {
				return "null";
			}else {
				return data.toString();
			}
		}
		
	}
	private Node<T> header,tail;
	private int size;
	
	/**
	 * 添加节点
	 * @param data
	 */
	public void addNode(T data) {
		/*
		 * 此处装逼失败本来应该这样写的null==header && tail==null本代码写法导致
		 * 每次调用addNode方法的时候导致tail重新赋值到header上检查的时候只关注是否等于null而没有关注到赋值
		 * 从而导致链表元素变少，但是size正确
		 * */
		
		if(null==(header = tail)) {
			header = tail = new Node<T>(null, data);
		}else {
			tail.next = new Node<>(null, data);
			tail = tail.next;
		}
		size++;
	}
	
	/**
	 * 反转链表
	 */
	public void revorse(){
		if(size == 0 || size == 1) {
			return;
		}
		tail = header;
		Node<T> pre = null;
		Node<T> curNode = header;
		Node<T> nextNode = header.next;
		while(nextNode != null) {
			curNode.next = pre;
			pre = curNode;
			curNode = nextNode;
			nextNode = curNode.next;
		}
		curNode.next = pre;
		header = curNode;
	}
	
	
	@Override
	public String toString() {
		Node<T> curNode = header;
		if(curNode == null) {
			return "null";
		}
		StringBuilder datas = new StringBuilder();
		T data = curNode.data;
		if(data == null) {
			datas.append("null");
		}else {
			datas.append(data.toString());
		} 
		Node<T> next = curNode.next;
		while(next != null) {
			data = next.data;
			if(data == null) {
				datas.append("null");
			}else {
				datas.append(data.toString());
			} 
			next = next.next;
		}
		return datas.toString();
	}

	public static void main(String[] args) {
		LinkList<String> ls = new LinkList<>();
		ls.addNode("a");
		ls.addNode("b");
		ls.addNode("c");
		ls.addNode("d");
		System.out.println(ls);
		ls.revorse();
		System.out.println(ls);
	}
}
