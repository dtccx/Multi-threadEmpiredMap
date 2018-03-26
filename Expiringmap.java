package test;

import java.util.HashMap;

//给一个expiring map， 你可以一直往里面put东西，这些东西都有个过期值，一旦过期就get不到了。
//put(null, null, 2000);

//10:00:00 - put(10, 25, 5000)
/**
* (10, (25, 10:00:05))
* 
//10:00:04 - get(10) -> 25
//10:00:05 - get(10) -> null
//10:00:06 - get(10) -> null
 * 
 * @author ccx
 *
 */
public class Expiringmap{
	static HashMap<Integer, Duration> map= new HashMap<Integer, Duration>();
	static private class Duration{
		private Integer value;
		private long time;
		public Duration(Integer value, long durtime) {
			this.value = value;
			this.time = durtime + System.currentTimeMillis(); 
		}
	}
	
	static void put(Integer key, Integer value, long durtime) {
		map.put(key, new Duration(value, durtime));
	}
	
	static Integer get(Integer key) {
		if(!map.containsKey(key))
			return null;
		if(System.currentTimeMillis() <= map.get(key).time)
			return map.get(key).value;
		else {
			map.remove(key);
			return null;
		}
	}
	public static void main(String[] args) {
		put(10, 25, 5000);
		try {
			Thread.sleep(4000);
			System.out.print("线程睡眠4秒！\n");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(get(10));
	}
}


/*
 * Follow 1:如果在4个小时内你的程序挂了，可能是什么原因?
 	Answer: 一直put，不get 内存会挂
 */

/*
 * 第二个 follow up： 如果用heap的话可以解决上面的问题，但是put时间变成了 O(k log n）怎么办？
在put的时候remove 已经expired的东西，这样会remove k个已经expired的东西，每次remove时间是log n， 所以put 从原来o（1） 变成了O(k log n）
 */

/*
 * 第三 follow up： put 从原来o（1） 变成了O(k log n）， 怎么解决？
lz说用多线程，put 只是put 到 hashmap 和 heap 里面， 但是不从heap remove。 put 从原来的 O(1）变成了 O(log n）
然后用另外一个线程来做remove 操作。（幸好没叫写多线程代码，不然死定了）
 */

