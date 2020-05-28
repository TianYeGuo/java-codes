package lamda;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author TianGuo
 * @version 1.0
 * @className LamdaTest
 * @Date 2020/4/26
 */
public class LamdaTest {
	public static void main(String[] args) {
		LocalDate now = LocalDate.now().plusMonths(1L);
		System.out.println(now.toString());
		/*
		 * stream方法获取指向当前Collection对象的流对象，filter将对流中元素进行过滤，结合lambda表达式，需要在filter参数中实现
		 * 一个类似于比较器的Predicate对象，返回一个boolean类型返回值，只有返回为true的Collection中的元素才会进入到forEach的循环中。
		 */
		List<String> strArr = Arrays.asList("21", "22", "3", "4");
		strArr.stream().filter(str -> {
			return str.startsWith("2");
		}).filter(str -> {
			return str.equals("22");
		}).forEach(str -> {
			System.out.println(str);

		});
		/**
		 * 输出：2
		 */
		strArr.forEach(str->System.out.println(str));
		System.out.println("----------------");
		List<String> collect = strArr.stream().filter(str ->
			 str.startsWith("2")
		).collect(Collectors.toList());
		collect.forEach(str->System.out.println(str));

		System.out.println("----------------");
		strArr.forEach(str->System.out.println(str));
	}
}
