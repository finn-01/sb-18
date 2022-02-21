


> Written with [StackEdit](https://stackedit.io/).
 # Hướng dẫn chi tiết Test Spring Boot
 Hôm nay chúng ta sẽ tìm hiểu cách để viết TestCase trong **Spring Boot**.

Yêu cầu tối thiếu để đi tiếp phần này đó là bạn phải có nền tảng với `JUnit` và `Mockito`.

Tôi nghĩ `JUnit` thì chắc ai cũng biết rồi, nhưng `Mockito` có lẽ sẽ có bạn lạ lẫm, nếu bạn chưa nghe tới nó bao giờ thì cũng đừng lo, có thể đọc viết [hướng dẫn Mockito chi tiết](https://loda.me/test-huong-dan-toan-tap-mockito-loda1576641016810) bởi luda :v

### **Vấn đề Test + Spring**

Chúng ta đều biết rằng **Spring Boot** sẽ phải tạo Context và tìm kiếm các Bean và nhét vào đó. Sau tất cả các bước config và khởi tạo thì chúng ta sử dụng `@Autowired` để lấy đối tượng ra sử dụng.

Vấn đề đầu tiên bạn nghĩ tới khi viết Test sẽ là làm sao `@Autowired` bean vào class Test được và làm sao cho `JUnit` hiểu `@Autowired` là gì.

Hướng giải quyết là tích hợp `Spring` vào với `JUnit`.
### **@RunWith(SpringRunner.class)**

**Spring Boot** đã thiết kế ra lớp `SpringRunner`, sẽ giúp chúng ta tích hợp **Spring + JUnit**.

Để test trong Spring, trong mọi class Test chúng ta sẽ thêm `@RunWith(SpringRunner.class)` lên đầu Class Test đó.

```java
@RunWith(SpringRunner.class)
public class TodoServiceTest {
    ...
}
```

Khi bạn chạy `TodoServiceTest` nó sẽ tạo ra một `Context` riêng để chứa `bean` trong đó, vậy là chúng ta có thể `@Autowired` thoải mái trong nội hàm Class này.

Vấn đề tiếp theo là làm sao đưa `Bean` vào trong `Context`.

Có 2 cách

1.  `@SpringBootTest`
2.  `@TestConfiguration`

### **@SpringBootTest**

`@SpringBootTest` sẽ đi tìm kiếm class có gắn `@SpringBootApplication` và từ đó đi tìm toàn bộ `Bean` và nạp vào `Context`.

Cái này chỉ nên sử dụng trong trường hợp muốn Integration Tests, vì nó sẽ tạo toàn bộ `Bean`, không khác gì bạn chạy cả cái `SpringApplication.run(App.class, args);`, rất tốn thời gian mà rất nhiều `Bean` thừa thãi, không cần dùng đến cũng khởi tạo.

```
@RunWith(SpringRunner.class)
@SpringBootTest
public class TodoServiceTest {

    /**
     * Cách này sử dụng @SpringBootTest
     * Nó sẽ load toàn bộ Bean trong app của bạn lên,
     * Giống với việc chạy App.java vậy
     */

    @Autowired
    private TodoService todoService;
}

```

### **@TestConfiguration**

`@TestConfiguration` giống với `@Configuration`, chúng ta tự định nghĩa ra `Bean`.

Các Bean được tạo bởi `@TestConfiguration` chỉ tồn tại trong môi trường Test. Rất phụ hợp với việc viết UnitTest.

Class Test nào, cần Bean gì, thì tự tạo ra trong `@TestConfiguration`

```java
@RunWith(SpringRunner.class)
public class TodoServiceTest2 {

    /**
     * Cách này sử dụng @TestConfiguration
     * Nó chỉ tạo ra Bean trong Context test này mà thôi
     * Tiết kiệm thời gian hơn khi sử dụng @SpringBootTest (vì phải load hết Bean lên mà không dùng đến)
     */
    @TestConfiguration
    public static class TodoServiceTest2Configuration{

        /*
        Tạo ra trong Context một Bean TodoService
         */
        @Bean
        TodoService todoService(){
            return new TodoService();
        }
    }

    @Autowired
    private TodoService todoService;
```
### **@MockBean**

**Spring** hỗ trợ mock với annotation `@MockBean`, chúng ta có thể mock lấy ra một `Bean` "giả" mà không thèm để ý tới thằng `Bean` "thật". (Kể cả thằng "thật" có ở trong Context đi nữa, cũng không quan tâm).

```java
@RunWith(SpringRunner.class)
public class TodoServiceTest2 {

    /**
     * Đối tượng TodoRepository sẽ được mock, chứ không phải bean trong context
     */
    @MockBean
    TodoRepository todoRepository;
```
