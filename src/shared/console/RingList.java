package shared.console;

import java.util.Iterator;

@SuppressWarnings("unchecked")
public class RingList<E> implements Iterable<E>{
	private int size;
	private int capacity;
	private Element first;
	private Element last;
	
	public RingList(int capacity){
		this.size = 0;
		this.capacity = capacity;
	}

	public synchronized void add(E e) {
		Element el = new Element(e);
		if(this.last != null) {
			this.last.next = el;
			this.last = el;
		}else {
			this.first = el;
			this.last = el;
		}
		if(full()) {
			el = this.first.next;
			this.first.next = null;
			this.first = el;
		}else {
			size++;
		}
	}
	
	public synchronized E get(int index) {
		if(index >= this.size || index < 0) {
			throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
		}
		Element el = this.first;
		for(int i = 0; i < index; i++) {
			el = el.next;
		}
		return (E)el.content;
	}
	
	public synchronized int size() {
		return this.size;
	}
	
	public synchronized boolean full() {
		return this.size >= this.capacity;
	}
	
	public synchronized E[] toArray() {
		Object[] array = new Object[this.size];
		Element el = this.first;
		for(int i = 0; el != null ; i++) {
			array[i] = el.content;
			el = el.next;
		}
		return (E[])array;
	}
	
	
	@Override
	public synchronized Iterator<E> iterator() {
		
		return new Iterator<E>() {
			Element el = first;
			@Override
			public boolean hasNext() {
				return el != null;
			}

			@Override
			public E next() {
				E e = (E)el.content;
				el = el.next;
				return e;
			}
		};
	}
}
