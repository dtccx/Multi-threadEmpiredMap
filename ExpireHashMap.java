package test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

//follow up 1 : 定期去clean已经expired的data
//用priority queue, ordered by expired time。每当线程启动器时，就与first进行比较，不断出队。

class Data<V> {// generic type
	V val;
	long startTime;
	long duration;

	public Data(V val, long duration, long startTime) {
		this.val = val;
		this.duration = duration;
		this.startTime = startTime;
	}
}

public class ExpireHashMap<K, V> extends Thread{
	Map<K, Data<V>> map = new HashMap<>();
	Queue<K> q = new PriorityQueue<K>(new Comparator<K>() {
		@Override
		public int compare(K o1, K o2) {
			if (getEndTime(o1) < getEndTime(o2)) {
				return -1;
			} else {
				return 1;
			}
		}

		private long getEndTime(K k) {
			return map.get(k).duration + map.get(k).startTime;
		}
	});

	// put
	public void put(K key, V val, long duration) {
		synchronized (this) {
			long startTime = System.currentTimeMillis();
			System.out.println("put <" + key + "," + val + "> into map at:" + startTime);
			Data<V> data = new Data<V>(val, duration, startTime);
			q.offer(key);
			map.put(key, data);
		}
	}

	// get
	public V get(K key) {
		synchronized (this) {
			Data<V> data = map.get(key);
			if (data == null) {
				System.out.println("the key:" + key + "was expired!");
				return null;
			}
			// if(data.startTime + data.duration <= System.currentTimeMillis()){
			// map.remove(key);
			//
			// return null;
			// }
			return (V) data.val;
		}
	}
	

	// is expired
	private boolean isExpired(K k) {
		synchronized (this) {
			if (System.currentTimeMillis() > map.get(k).duration + map.get(k).startTime) {
				return true;
			}
			return false;
		}
	}

	// remove  
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (true) {
			// System.out.println("started!");
			while (!q.isEmpty()) {
				K k = q.peek();
				if (isExpired(k)) {
					map.remove(k);
					q.poll();
				}
			}
		}
	}

	// current thread sleep for ten seconds
	public static void tenseconds(ExpireHashMap<Integer, Integer> obj) {
		System.out.println("waiting for 10 seconds...");
		for (int i = 0; i < 10; i++) {
			try {
				System.out.println("round" + i + "get:" + obj.get(1));
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		ExpireHashMap<Integer, Integer> obj = new ExpireHashMap<>();
		Thread ta = new Thread(obj, "RemoveExpiredKeyThread");
		ta.start();
		obj.put(1, 2, 5000);// it will expired after 2s
		tenseconds(obj);
		System.exit(0);
	}
}