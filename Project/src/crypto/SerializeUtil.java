package crypto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializeUtil {
    // 객체 직렬화 (Object -> byte[])
    public static byte[] objectToBytes(Object obj) throws IOException {
         if (obj == null) {
            return new byte[0];
        }
        
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
            return bos.toByteArray();
        }
    }

    // 객체 역직렬화 (byte[] -> Object)
    public static Object bytesToObject(byte[] data) throws IOException, ClassNotFoundException {
       if (data == null || data.length == 0) {
            return null;
        }
        
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            ObjectInputFilter filter = ObjectInputFilter.Config.createFilter("model.*;entity.*;java.base/*;!*");
            ois.setObjectInputFilter(filter);
            return ois.readObject();
        }
    }
}
