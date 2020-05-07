package juc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
/**
 * 通过对象的key来过滤对象
 * @author TianGuo
 * @version  2020/5/7
 * @param null
 * @return
 * @see
 */
public class JavaStreamDistinctExamples {
	public static void main(String[] args) {
		Person lokesh = new Person(1, "Lokesh", "Gupta");
		Person brian = new Person(2, "Brian", "Clooney");
		Person alex = new Person(3, "Lok444esh", "Kolen");
		Person lokesh1 = new Person(4, "Lok444esh", "Gup444ta1");

		// Add some random persons
//		List<Person> list = Arrays.asList(lokesh,lokesh1, brian, alex, lokesh, brian, lokesh);
		List<Person> list = Arrays.asList(lokesh1,brian, alex,lokesh);
		System.out.println(list);
		// 过滤对象
		// Get distinct only
		List<Person> distinctElements = list.stream().filter(distinctByKey(p -> p.getFname()))
				.collect(Collectors.toList());
		/*
		 * 
		 * List<Person> distinctElements = list.stream().filter(distinctByKey(new
		 * Function<Person, Object>() {
		 * 
		 * @Override public Object apply(Person person) { return person.getId(); }
		 * })).collect(Collectors.toList());
		 */
		// Let's verify distinct elements
		System.out.println(distinctElements);
		// 获取最大的char或者String
		String maxChar = Stream.of("H", "T", "D", "I", "J").max(Comparator.comparing(String::valueOf)).get();
		String maxChar2 = Stream.of("H", "T", "D", "I", "J").max(Comparator.comparing(w1 -> String.valueOf(w1))).get();
		String maxChar3 = Stream.of("H", "T", "D", "I", "J")
				.max((q1, q2) -> String.valueOf(q1).compareTo(String.valueOf(q2))).get();

		String minChar = Stream.of("H", "T", "D", "I", "J").min(Comparator.comparing(String::valueOf)).get();
		String minChar2 = Stream.of("H", "T", "D", "I", "J").min(String::compareTo).get();

		System.out.println("maxChar = " + maxChar);
		System.out.println("minChar = " + minChar);
		System.out.println("maxChar3 = " + maxChar3);
		// 获取最大和最小数
		Integer maxNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).max(Comparator.comparing(Integer::valueOf)).get();
		Integer minNumber = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9).min(Comparator.comparing(Integer::valueOf)).get();

		System.out.println("maxNumber = " + maxNumber);
		System.out.println("minNumber = " + minNumber);
		
		
		// 获得最大、最小的日期
		LocalDate start = LocalDate.now();
		LocalDate plusDays = start.plusDays(1);
		System.out.println("plusdats"+plusDays);
//		TemporalAdjusters时间调节器
		LocalDate end = LocalDate.now().plusMonths(1).with(TemporalAdjusters.lastDayOfMonth()).plusDays(1);
		System.out.println(start+"==="+end);
		Long t= 1l;
		//lambda 可以访问外部变量
		/*
		 * . iterate方法：也是生成无限长度的Stream，和generator不同的是，其元素的生成是重复对给定的种子值(seed)调用用户指定函数来生成的。
		 * 其中包含的元素可以认为是：seed，f(seed),f(f(seed))无限循环
				Stream.iterate(1, item -> item + 1).limit(10).forEach(System.out::println);
				这段代码就是先获取一个无限长度的正整数集合的Stream，然后取出前10个打印。千万记住使用limit方法，不然会无限打印下去。
		 */
		/*
		 * 一start开始日期为种子，以一天为基数增加   迭代生成无限长度的日期
		 */
		long between = ChronoUnit.DAYS.between(start,end);
		System.out.println(between);
		List<LocalDate> dates = Stream.iterate(start, date -> date.plusDays(t))
				//计算出两点之间的时间量，例如ChronoUnit.DAYS.between(t1, t2)
				.limit(ChronoUnit.DAYS.between(start, end)).collect(Collectors.toList());
		System.out.println(dates);
		 DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyyMMdd");
		String collect = Stream.iterate(start, date -> date.plusDays(t))
            //计算出两点之间的时间量，例如ChronoUnit.DAYS.between(t1, t2)
            .limit(ChronoUnit.DAYS.between(start, end)).map(mapper->mapper.format(fmt)).collect(Collectors.joining(","));
		System.out.println(collect);
		// Get Min or Max Date
		LocalDate maxDate = dates.stream().max(Comparator.comparing(LocalDate::toEpochDay)).get();
		LocalDate minDate = dates.stream().min(Comparator.comparing(LocalDate::toEpochDay)).get();

		System.out.println("maxDate = " + maxDate);
		System.out.println("minDate = " + minDate);

		// 获取最大和最小的对象
		List<Employee> emps = new ArrayList<Employee>();

		emps.add(new Employee(1, "Lokesh", 36));
		emps.add(new Employee(2, "Alex", 46));
		emps.add(new Employee(3, "Brian", 52));

		Comparator<Employee> comparator = Comparator.comparing(Employee::getAge);

		// Get Min or Max Object
		Employee minObject = emps.stream().min(comparator).get();
		Employee maxObject = emps.stream().max(comparator).get();

		System.out.println("minObject = " + minObject);
		System.out.println("maxObject = " + maxObject);

	}
	/**
	 * 过滤对象
	 * @author TianGuo
	 * @version  2020/5/7
	 * @param keyExtractor
	 * @return java.util.function.Predicate<T>
	 * @see
	 */
	public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		// 通过ConcurrentHashMap来确定是否包含某个元素，其接受一个函数式对象的引用。
		/*
		 * put与putIfAbsent区别:

			put在放入数据时，如果放入数据的key已经存在与Map中，最后放入的数据会覆盖之前存在的数据，
			
			而putIfAbsent在放入数据时，如果存在重复的key，那么putIfAbsent不会放入值。

			putIfAbsent   如果传入key对应的value已经存在，就返回存在的value，不进行替换。如果不存在，就添加key和value，返回null
		 */
		Map<Object, Boolean> map = new ConcurrentHashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
//		return t -> map.put(keyExtractor.apply(t), Boolean.TRUE) == null;
	}
}

class Employee {
	private int id;
	private String name;
	private int age;

	public Employee(int id, String name, int age) {
		super();
		this.id = id;
		this.name = name;
		this.age = age;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@Override
	public String toString() {
		StringBuilder str = null;
		str = new StringBuilder();
		str.append("Id:- " + getId() + " Name:- " + getName() + " Age:- " + getAge());
		return str.toString();
	}
}

class Person {
	public Person(Integer id, String fname, String lname) {
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
	}

	private Integer id;
	private String fname;
	private String lname;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFname() {
		return fname;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public String getLname() {
		return lname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", fname=" + fname + ", lname=" + lname + "]";
	}
}