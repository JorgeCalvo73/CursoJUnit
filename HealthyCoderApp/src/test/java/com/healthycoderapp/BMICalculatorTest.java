package com.healthycoderapp;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class BMICalculatorTest {
	
	private String environment = "dev";
	
	@BeforeAll
	static void beforeAll() {
		System.out.println("Before all unit tests");
	}
	
	@AfterAll
	static void afterAll() {
		System.out.println("After all unit tests");
	}
	
	@Nested
	class IsDietRecommendedTests{
		
		//@ParameterizedTest
		//@ValueSource(doubles = {89.0, 95.0, 110.0})
		
		//@ParameterizedTest(name = "weight={0}, height={1}")
		//@CsvSource(value = {"89.0, 1.72", "95.0, 1.75", "110.0, 1.78"})
		
		@ParameterizedTest(name = "weight={0}, height={1}")
		@CsvFileSource(resources = "/diet-recommended-input-data.csv", numLinesToSkip = 1)
		void should_ReturnTrue_When_DietRecommended(Double coderWeight, Double coderHeight) {
			
			//given - Arrange
			double weight = coderWeight;
			double height = coderHeight;
			
			//when - Act
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			//then - Assert
			assertTrue(recommended);
		}
		
		@Test
		void should_ReturnFalse_When_DietNotRecommended() {
			
			//given - Arrange
			double weight = 50.0;
			double height = 1.92;
			
			//when - Act
			boolean recommended = BMICalculator.isDietRecommended(weight, height);
			
			//then - Assert
			assertFalse(recommended);
		}
		
		@Test
		void should_ThrowArithmeticException_When_DietNotRecommended() {
			
			// Para comprobar el funcionamiento de una excepci?n, necesitamos un executable
			
			//given - Arrange
			double weight = 50.0;
			double height = 0.0;
			
			//when - Act
			// Requiere una lambda
			Executable executable = () -> BMICalculator.isDietRecommended(weight, height);
			
			//then - Assert
			assertThrows(ArithmeticException.class, executable);
		}
	}
	
	@Nested
	class FindCoderWithWorstBMITests{
		
		@Test
		@DisplayName("Nombre del m?todo")
		@Disabled
		void should_ReturnCoderWithWorstBMI_When_CoderListNotEmpty() {
			
			//given - Arrange
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.80, 64.7));
	
			//when - Act
			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
			
			//then - Assert
			/* Si falla uno de los asserts, no se comprueba si el siguiente ha fallado.
			 * Para que se compruebe hay que envolver todo en assertAll, el cual necesita que 
			 * le pasemos una lambda por cada assert que quedamos que se compruebe */
			assertAll(
					() -> assertEquals(1.82, coderWorstBMI.getHeight()),
					() -> assertEquals(98.0, coderWorstBMI.getWeight())
			);
		}
		
		@Test
		void should_ReturnNullWorstBMI_When_CoderListEmpty() {
			
			//given - Arrange
			List<Coder> coders = new ArrayList<>();
					
			//when - Act
			Coder coderWorstBMI = BMICalculator.findCoderWithWorstBMI(coders);
			
			//then - Assert
			assertNull(coderWorstBMI);
		}
		
		@Test
		void should_ReturnCoderWithWorstBMIIn1Ms_When_CoderListHas10000Elements() {
			
			//given
			assumeTrue(BMICalculatorTest.this.environment.equals("prod"));
			List<Coder> coders = new ArrayList<>();
			for(int i=0; i<10000; i++) {
				coders.add(new Coder(1.0 + i, 10.0 + i));
			}
			
			// when
			Executable executable = () -> BMICalculator.findCoderWithWorstBMI(coders);
			
			//then
			assertTimeout(Duration.ofMillis(500), executable);
		}
	}
	
	@Nested
	class GetBMIScoresTests{
		
		@Test
		void should_ReturnCorrectBMIScoreArray_When_CoderListNotEmpty() {
			
			//given - Arrange
			List<Coder> coders = new ArrayList<>();
			coders.add(new Coder(1.80, 60.0));
			coders.add(new Coder(1.82, 98.0));
			coders.add(new Coder(1.82, 64.7));

			double[] expected = {18.52, 29.59, 19.53};
			
			//when - Act
			double[] bmiScores = BMICalculator.getBMIScores(coders);
			
			//then - Assert
			assertArrayEquals(expected, bmiScores);
		}
	}

	
	
	
	
	

}
