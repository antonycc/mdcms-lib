package uk.co.diyaccounting.persistence.unit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.LoggerFactory;

/**
 * Test java splt
 *
 * @author Antony
 */
public class SplitTest {

   private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SplitTest.class);

	public static class Student{
		public String firstName;
		public String surname;
		public int number;
		public String field04;
		public String field05;
		public String field06;
		public String field07;
		public String field08;
		public String field09;
		public String field10;
		public String field11;
	}

   @Test
   public void expectToFindAMatchForFirstNameAndSurname() {

      // Test parameters
      String record = "Barrett,Zulema,848284,M,3A1,3H2,S3,961025,2891374756, ,CHT3O0ACLU3M0BENG3C0EHIR3C0AMBF3C0CPPL3OMAPPL4OMCSBI3C";
		String firstNameToFind = "Zulema";
		String surnameToFind = "Barrett";

		// Split String
	   String[] fields = record.split(",");
	   Student student = new Student();
	   if (fields.length >= 1) student.surname = fields[0];
	   if (fields.length >= 2) student.firstName = fields[1];
	   if (fields.length >= 3) student.number = Integer.parseInt(fields[2]);
	   if (fields.length >= 4) student.field04 = fields[3];
	   if (fields.length >= 5) student.field05 = fields[4];
	   if (fields.length >= 6) student.field06 = fields[5];
	   if (fields.length >= 7) student.field07 = fields[6];
	   if (fields.length >= 8) student.field08 = fields[7];
	   if (fields.length >= 9) student.field09 = fields[8];
	   if (fields.length >= 10) student.field10 = fields[9];
	   if (fields.length >= 11) student.field11 = fields[10];

	   // Check with names the correct way round
	   Assert.assertTrue(firstNameToFind.equals(student.firstName));
	   Assert.assertTrue(surnameToFind.equals(student.surname));
	   Assert.assertTrue(firstNameToFind.equals(student.firstName) && surnameToFind.equals(student.surname));
	   Assert.assertTrue(record.indexOf(firstNameToFind) != -1 && record.indexOf(surnameToFind) != -1);

	   // Check with first name and surname reversed
	   Assert.assertFalse(firstNameToFind.equals(student.surname));
	   Assert.assertFalse(surnameToFind.equals(student.firstName));
	   Assert.assertFalse(firstNameToFind.equals(student.surname) && surnameToFind.equals(student.firstName));
	   Assert.assertTrue(record.indexOf(surnameToFind) != -1 && record.indexOf(firstNameToFind) != -1);
   }
}