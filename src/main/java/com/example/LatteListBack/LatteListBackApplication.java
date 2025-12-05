package com.example.LatteListBack;
import com.example.LatteListBack.Services.CafeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

//Cuando este todo avanzado hay que sacar esto del miny aparte agregar de nuevo la dependencia de SpringSegurity en el pom
//Esto es solo para pruebas
@SpringBootApplication
public class LatteListBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(LatteListBackApplication.class, args);
	}
	@Bean
	public CommandLineRunner init(CafeService cafeService) {
		return args -> {
			System.out.println("... Iniciando verificación de sincronización al arrancar la app ...");
			cafeService.actualizarCafesDesdeApi();
		};
	}
}


/*
@Autowired
	private CafeService cafeService;
	@Bean
	public CommandLineRunner initData() {
		return args -> {
			System.out.println("--- INICIANDO ACTUALIZACIÓN DE CAFÉS DESDE API ---");
			try {
				cafeService.actualizarCafesDesdeApi();
				System.out.println("--- ACTUALIZACIÓN COMPLETADA. ---");

			} catch (Exception e) {
				System.err.println("--- ERROR AL ACTUALIZAR CAFÉS DESDE API: " + e.getMessage() + " ---");
				e.printStackTrace();
			}
		};
	}*/


/*<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-security</artifactId>
		</dependency>*/