package com.company.books.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.books.backend.model.Libro;
import com.company.books.backend.model.dao.ILibroDao;
import com.company.books.backend.response.LibroResponseRest;

@Service
public class LibroServiceImpl implements IlibroService{

	private static final Logger log = LoggerFactory.getLogger(LibroServiceImpl.class);
	
	@Autowired
	private ILibroDao ILibroDao;
	
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> ObtenerLibros() {
		
		log.info("Inicio metodo obtenerLibros()");
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			
			List<Libro> libro = (List<Libro>) ILibroDao.findAll();
			
			response.getLibroResponse().setLibro(libro);
			
			response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
			
		}catch(Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al consulta categorias");
			log.error("Error al consultar libros: ", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}


	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<LibroResponseRest> buscarPorId(Long id) {
		
		
		log.info("Inicio metodo buscar Por Id");
		
		LibroResponseRest response = new LibroResponseRest();
		
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Optional<Libro> libro = ILibroDao.findById(id);
			
			if(libro.isPresent()) {
				list.add(libro.get());
				response.getLibroResponse().setLibro(list);
			}else {
				log.error("Error en consultar categoria");
				response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		}catch(Exception e) {
			log.error("Error en consultar categoria");
			response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
		}
		response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}


	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> crear(Libro libro) {
		
		log.info("Inicio metodo Crear libro");
		
		LibroResponseRest response = new LibroResponseRest();
		
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Libro libroGuardado = ILibroDao.save(libro);
			
			if( libroGuardado != null) {
				list.add(libroGuardado);
				response.getLibroResponse().setLibro(list);
			} else {
				log.error("Error en grabar libro");
				response.setMetadata("Respuesta nok", "-1", "Categoria no creada");
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
			
			}
			
		}catch(Exception e) {
			log.error("Error en crear Libro");
			response.setMetadata("Respuesta nok", "-1", "Error al grabar Libro");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
		}
		
		response.setMetadata("Respuesta ok", "00", "Libro creado");
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}


	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> actualizar(Libro libro, Long id) {
		// TODO Auto-generated method stub
		
		log.info("Inicio metodo actualizar");
		
		LibroResponseRest response = new LibroResponseRest();
		
		List<Libro> list = new ArrayList<>();
		
		try {
			
			Optional<Libro> LibroBuscado = ILibroDao.findById(id);
			
			if ( LibroBuscado.isPresent()) {
				
				LibroBuscado.get().setNombre(libro.getNombre());
				LibroBuscado.get().setDescripcion(libro.getDescripcion());
				LibroBuscado.get().setCategoria(libro.getCategoria());
				
				Libro LibroActualizar = ILibroDao.save(LibroBuscado.get());
				
				if(LibroActualizar!=null) {
					response.setMetadata("Respuesta ok", "00", "Categoria Actualizada");
					list.add(LibroActualizar);
					response.getLibroResponse().setLibro(list);
				}else {
					log.error("Error en actualizar Libro");
					response.setMetadata("Respuesta nok", "-1", "Libro no creado");	
					return new ResponseEntity<LibroResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
			}else {
				log.error("Error en actualizar Libro");
				response.setMetadata("Respuesta nok", "-1", "Libro no creado");	
				return new ResponseEntity<LibroResponseRest>(response, HttpStatus.NOT_FOUND);
			
			}
			
			
		}catch ( Exception e ) {
			log.error("error en actualizar libro", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "-1", "libro no creado");
			return new ResponseEntity<LibroResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
		
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}


	@Override
	@Transactional
	public ResponseEntity<LibroResponseRest> eliminar(Long id) {
		
		log.info("Inicio del metodo eliminar libro");
		
		LibroResponseRest response = new LibroResponseRest();
		
		try {
			ILibroDao.deleteById(id);
			response.setMetadata("respuesta ok", "00", "Libro Eliminado");
		}catch (Exception e) {
			log.error("Error en actualizar Libro",e.getMessage());
		}
		
		return new ResponseEntity<LibroResponseRest>(response, HttpStatus.OK);
	}
	




}
