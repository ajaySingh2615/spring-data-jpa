package com.example.demo;

import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.time.LocalDateTime;
import java.util.List;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(
            StudentRepository studentRepository,
            StudentIdCardRepository studentIdCardRepository
    ){
        return args -> {
//            generateRandomStudents(studentRepository);
//            sorting(studentRepository);

//            PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("firstName").ascending());
//            Page<Student> page = studentRepository.findAll(pageRequest);
//            System.out.println(page);

            Faker faker = new Faker();
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55)
            );

            student.addBook(new Book(LocalDateTime.now().minusDays(4), "Clean Code"));
            student.addBook(new Book(LocalDateTime.now(), "Think and grow"));
            student.addBook(new Book(LocalDateTime.now().minusDays(1), "Atomic habits"));

            StudentIdCard studentIdCard = new StudentIdCard("123456789", student);
            student.setStudentIdCard(studentIdCard);

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 1L),
                    student,
                    new Course("Computer Science", "IT"),
                    LocalDateTime.now()
            ));

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 2L),
                    student,
                    new Course("History", "UPSE"),
                    LocalDateTime.now()
            ));
//            student.enrolTOCourse(new Course("Computer Science", "IT"));
//            student.enrolTOCourse(new Course("History", "UPSE"));

            studentRepository.save(student);

            studentRepository.findById(1L)
                            .ifPresent(s -> {
                                System.out.println("Fetch books lazy...");
                                List<Book> books = student.getBooks();
                                books.forEach(book -> {
                                    System.out.println(s.getFirstName() + " borrowed " + book.getBook_name());
                                });
                            });
//
//            studentIdCardRepository.findById(1L)
//                    .ifPresent(System.out::println);

//            studentRepository.deleteById(1L);

        };
    }

//    private static void sorting(StudentRepository studentRepository) {
//        Sort sort = Sort.by( "firstName").ascending().and(Sort.by("age").descending());
//        studentRepository.findAll(sort)
//                .forEach(student -> System.out.println(student.getFirstName() + " " + student.getAge()));
//    }

    private static void generateRandomStudents(StudentRepository studentRepository) {
        Faker faker = new Faker();
        for(int i=0; i<20; i++){
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = String.format("%s.%s@gmail.com", firstName, lastName);
            Student student = new Student(
                    firstName,
                    lastName,
                    email,
                    faker.number().numberBetween(17, 55)
            );
            studentRepository.save(student);
        }
    }
}
