package com.example.uploadingfiles.util;

import com.example.uploadingfiles.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class DbInit implements CommandLineRunner {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public DbInit(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /*
    Раскомментировать стоит, если нужно очистить предыдущих пользователей и заполнить базу подготовленными
    заранее, в других случаях должно оставаться закомментированным
     */
    @Override
    public void run(String... args) throws IOException {
        // Delete all
//        this.userRepository.deleteAll();
//
//        // Create users
//        User dan = new User("dan",passwordEncoder.encode("dan123"),"USER","");
//        User admin = new User("admin",passwordEncoder.encode("admin123"),"ADMIN","");
//        User moderator = new User("moderator",passwordEncoder.encode("moderator123"),"MODERATOR","");
//
//        List<User> users = Arrays.asList(dan,admin,moderator);
//
//        // Save to db
//        this.userRepository.saveAll(users);
//        ObjectMapper mapper = new XmlMapper();
//        mapper.enable(SerializationFeature.INDENT_OUTPUT);
//        String xmlStringDan = mapper.writeValueAsString(dan);
//        String xmlStringAdmin = mapper.writeValueAsString(admin);
//        String xmlStringModerator = mapper.writeValueAsString(moderator);
//        String xmlString = xmlStringDan + xmlStringAdmin + xmlStringModerator;
//        storeUsersInFile(xmlString);
    }

    /*  Сохраняет информацию о пользователях в файл security.xml, на вход получает строку, которая генерируется
        библиотекой XML и убирает ненужные теги (permissionList и roleList), добавляя нужные (users), чтобы потом
        работало чтение этих данных из файла корректно
    */
    public static void storeUsersInFile(String xmlString) throws IOException {
        xmlString = "<users>\n" + xmlString.replaceAll("<permissionList/>", "")
                .replaceAll("<roleList>\\s", "")
                .replaceAll("\\s</roleList>", "")
                .replaceAll("<roleList>.*?</roleList>", "").replaceAll(">\\s{10,}<", ">\n<") + "</users>";

        FileWriter writer = new FileWriter("complete\\src\\main\\java\\com\\example\\uploadingfiles\\security\\security.xml", false);
        writer.write(xmlString);
        writer.close();
    }
}