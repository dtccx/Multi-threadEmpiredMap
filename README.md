# Multi-threadEmpiredMap

Implement the expired map function, given a expiring map, put value into it with an empire date, once data empired, can't get.

# Tricky
If someone always put, the memory will die. So,

1. using Heap to deal with empired map.
2. but then the put time will be O(klogn)
3. using multi-thread to do put, optimze the procedure.


# Here is Chinese explaination, if you understand Chinese:
//给一个expiring map， 你可以一直往里面put东西，这些东西都有个过期值，一旦过期就get不到了。
//put(null, null, 2000);

Tricky:
1:如果在4个小时内你的程序挂了，可能是什么原因?
 	Answer: 一直put，不get 内存会挂
 */

/*
2. 如果用heap的话可以解决上面的问题，但是put时间变成了 O(k log n）怎么办？
在put的时候remove 已经expired的东西，这样会remove k个已经expired的东西，每次remove时间是log n， 所以put 从原来o（1） 变成了O(k log n）
 */

/*
3 put 从原来o（1） 变成了O(k log n）， 怎么解决？
lz说用多线程，put 只是put 到 hashmap 和 heap 里面， 但是不从heap remove。 put 从原来的 O(1）变成了 O(log n）
然后用另外一个线程来做remove 操作。
 */
