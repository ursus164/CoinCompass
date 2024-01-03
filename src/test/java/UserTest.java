import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

public class UserTest {
    private static final String userList_cache = "src/cache/user_data.json";
    public static void main(String[] args) {
        User newUser = new User("siemson", "siemano");
        addUserToJson(newUser);
        // Sprawdzenie, czy użytkownik już istnieje
        List<User> existingUsers = UserList.convert();
        System.out.println(existingUsers);
        UserList userList = new UserList(existingUsers);
        System.out.println(userList.getByUsername("siemson"));
        Optional<User> existingUser = userList.getByUsername(newUser.getUsername());


//        if (!existingUser.isPresent()) {
//            addUserToJson(newUser);
//        } else {
//            System.out.println("Użytkownik o nazwie " + newUser.getUsername() + " już istnieje.");
//        }
    }

    private static void addUserToJson(User newUser) {
        JSONArray jsonArray;
        try {
            File file = new File(userList_cache);
            if (file.exists() && file.length() != 0) {
                String userJson = new String(Files.readAllBytes(Paths.get(userList_cache)));
                jsonArray = new JSONArray(userJson);
            } else {
                jsonArray = new JSONArray(); // Utwórz nową pustą listę JSON, jeśli plik nie istnieje lub jest pusty
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", newUser.getUsername());
            jsonObject.put("password", newUser.getPassword());
            jsonArray.put(jsonObject);

            try (FileWriter fileWriter = new FileWriter(userList_cache)) {
                fileWriter.write(jsonArray.toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
