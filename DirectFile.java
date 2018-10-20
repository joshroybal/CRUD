import java.io.*;

class DirectFile
{  static final int RECSIZ = 80;
   static final int FLDSIZ = 40;

   // insert record into file and maintain key order of file
   public static boolean insert(String filename, String record)
      throws IOException
   {  String key = record.substring(0, FLDSIZ).trim();
      RandomAccessFile iofile = new RandomAccessFile(filename, "rw");

      byte buffer[] = new byte[RECSIZ];
      long pos = iofile.length();
      while ( pos > 0 )
      {  iofile.seek(pos - RECSIZ);
         iofile.read(buffer);
         String prev = new String(buffer).substring(0, FLDSIZ).trim();
         if ( key.compareTo(prev) > 0 ) break;
         iofile.seek(pos);
         iofile.write(buffer);
         pos -= RECSIZ;
      }
      iofile.seek(pos);
      iofile.write(record.getBytes());
      iofile.close();
      return true;
   }

   // retrieve record
   public static String retrieve(String filename, String target)
      throws IOException
   {  File infile = new File(filename);
      RandomAccessFile directfile = new RandomAccessFile(infile, "rw");

      byte buffer[] = new byte[RECSIZ];
      long eof = directfile.length();
      long a = 0, b = eof/RECSIZ-1, m = b/2;

      while ( a <= b ) {
         directfile.seek(RECSIZ*m);
         directfile.read(buffer);
         String key = new String(buffer).substring(0, FLDSIZ).trim();
         if (target.compareTo(key) < 0) {
            b = m - 1;
         } else if (target.compareTo(key) > 0) {
            a = m + 1;
         } else {
            return new String(buffer).substring(FLDSIZ).trim();
         }
         m = (a + b) / 2;
      }
      directfile.close();
      return null;
   }

   public static boolean update(String filename, String record)
      throws IOException
   {  File infile = new File(filename);
      RandomAccessFile directfile = new RandomAccessFile(infile, "rw");

      String target = record.substring(0, FLDSIZ).trim();
      byte buffer[] = new byte[RECSIZ];
      long eof = directfile.length();
      long a = 0, b = eof/RECSIZ-1, m = b/2;

      while ( a <= b ) {
         directfile.seek(RECSIZ*m);
         directfile.read(buffer);
         String key = new String(buffer).substring(0, FLDSIZ).trim();
         if (target.compareTo(key) < 0) {
            b = m - 1;
         } else if (target.compareTo(key) > 0) {
            a = m + 1;
         } else { // match found - overwrite old record
            directfile.seek(RECSIZ*m);
            directfile.write(record.getBytes());
            return true;
         }
         m = (a + b) / 2;
      }
      directfile.close();
      return false;
   }

   public static boolean delete(String filename, String target)
      throws IOException
   {  File iofile = new File(filename);
      RandomAccessFile directfile = new RandomAccessFile(iofile, "rw");

      byte buffer[] = new byte[RECSIZ];
      long eof = directfile.length();
      long a = 0, b = eof/RECSIZ-1, m = b/2;

      while ( a <= b ) {
         directfile.seek(RECSIZ*m);
         directfile.read(buffer);
         String key = new String(buffer).substring(0, FLDSIZ).trim();
         if (target.compareTo(key) < 0) {
            b = m - 1;
         } else if (target.compareTo(key) > 0) {
            a = m + 1;
         } else { // match found - overwrite old record
            truncate(directfile, RECSIZ*m);
            directfile.close();
            return true;
         }
         m = (a + b) / 2;
      }
      directfile.close();
      return false;
   }

   // move records after deleted item up and reset file size
   public static void truncate(RandomAccessFile iofile, long pos)
      throws IOException
   {  byte[] buffer = new byte[RECSIZ];
      long eof = iofile.length();
      while ( pos < eof ) {
         iofile.seek(pos+RECSIZ);
         iofile.read(buffer);
         iofile.seek(pos);
         iofile.write(buffer);
         pos += RECSIZ;
      }
      iofile.setLength(eof-RECSIZ);
   }
}
