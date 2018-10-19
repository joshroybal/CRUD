import java.io.*;

class DirectFile
{  static final int RECSIZ = 80;
   static final int FLDSIZ = 40;

   // insert record into file and maintain key order of file
   public static boolean insert(String filename, String record)
      throws IOException
   {  String key = record.substring(0, FLDSIZ).trim();
      RandomAccessFile iofile = new RandomAccessFile(filename, "rw");

      String tmp = new String();
      String oldrec = new String();
      byte[] buffer = new byte[RECSIZ];
      long idx = 0;
      while ( idx < iofile.length() )  // find insertion point
      {  iofile.read(buffer);
         tmp = new String(buffer, 0, RECSIZ);
         if ( key.compareTo(tmp.substring(0, FLDSIZ).trim()) == 0 )
         {  iofile.close();
            return false;
         }
         if (key.compareTo(tmp.substring(0, FLDSIZ).trim()) < 0) break;
         idx += RECSIZ;
      }

      long tag = idx;
      long eof = iofile.length() + RECSIZ;

      idx = iofile.length();
      while ( idx > tag )
      {  iofile.seek(idx - RECSIZ);
         iofile.read(buffer);
         iofile.seek(idx);
         iofile.write(buffer);
         idx -= RECSIZ;
      }

      // insert the new record
      iofile.seek(tag);
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

   public static void truncate(RandomAccessFile iofile, long pos)
      throws IOException
   {  byte[] buffer = new byte[RECSIZ];
      long eof = iofile.length();
      while ( pos < eof ) {
         iofile.seek(pos+RECSIZ);
         iofile.read(buffer);
         iofile.seek(pos);
         iofile.write(buffer);
         System.out.println(new String(buffer).trim());
         pos += RECSIZ;
      }
      iofile.setLength(eof-RECSIZ);
   }
}
