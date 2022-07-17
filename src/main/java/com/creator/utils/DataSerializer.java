package com.creator.utils;

import java.io.*;
import java.util.Base64;
public class DataSerializer {

  public static String serialize ( Serializable instance  )
  throws Exception{
    try {
      ByteArrayOutputStream stream = new ByteArrayOutputStream() ;
      ObjectOutputStream out = new ObjectOutputStream(stream) ;
      out.writeObject(instance) ;
      out.flush();
      return Base64.getEncoder().encodeToString(stream.toByteArray());
    } catch( IOException e ) {
      throw new Exception(DataSerializer.class + " serialize IOERROR : " , e ) ;
    }
  }

  public static Object deserialize ( String serialized )
  throws SerializerException {
    try {
      byte [] decoded = Base64.getDecoder().decode(serialized); ;
      ByteArrayInputStream stream = new ByteArrayInputStream(decoded) ;
      ObjectInputStream in = new ObjectInputStream(stream) ;
      return in.readObject() ;
    } catch( IOException e ) {
      throw new SerializerException(DataSerializer.class + " deserialize IOERROR : "+e.getMessage() , e ) ;
    } catch (ClassNotFoundException e) {
      throw new SerializerException(DataSerializer.class + " deserialize ClassNotFound : "+e.getMessage()  , e ) ;
    }
  }
}