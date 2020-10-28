package main;

import com.owlike.genson.Context;
import com.owlike.genson.Converter;
import com.owlike.genson.stream.ObjectReader;
import com.owlike.genson.stream.ObjectWriter;

public class UserConverter implements Converter<User> {

    @Override
    public void serialize(User user, ObjectWriter writer, Context ctx) {
        writer.beginObject();
        writer.writeNumber("id", user.getId())
                .writeString("email", user.getEmail())
                .writeString("password", user.getPassword())
                .writeString("firstName", user.getFirstName())
                .writeString("lastName", user.getLastName())
                .writeString("house", user.getHouse());
        writer.endObject();
    }

    @Override
    public User deserialize(ObjectReader reader, Context ctx) {
        User user = new User();
        reader.beginObject();

        while (reader.hasNext()) {
            reader.next();
            if ("id".equals(reader.name())) user.setId(reader.valueAsInt());
            else if ("email".equals(reader.name())) user.setEmail(reader.valueAsString());
            else if ("password".equals(reader.name())) user.setPassword(reader.valueAsString());
            else if ("firstName".equals(reader.name())) user.setFirstName(reader.valueAsString());
            else if ("lastName".equals(reader.name())) user.setLastName(reader.valueAsString());
            else if ("house".equals(reader.name())) user.setHouse(reader.valueAsString());
            else reader.skipValue();
        }

        reader.endObject();
        return user;
    }

}
