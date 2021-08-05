package com.company.books.backend.service;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.books.backend.model.Categoria;
import com.company.books.backend.model.dao.ICategoriaDao;
import com.company.books.backend.response.CategoriaResponseRest;

@Service
public class CategoriaServiceImpl implements ICategoriaService {

	private static final Logger log = LoggerFactory.getLogger(CategoriaServiceImpl.class);
	
	
	@Autowired
	private ICategoriaDao categoriaDao;
	
	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoriaResponseRest> buscarCategorias() {
		
		log.info("inicio metodo buscarCategoria()");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		try {
			
			List<Categoria> categoria = (List<Categoria>) categoriaDao.findAll();
			
			response.getCategoriaResponse().setCategoria(categoria);
			
			response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
			
		}catch(Exception e) {
			response.setMetadata("Respuesta nok", "-1", "Error al consulta categorias");
			log.error("Error al consultar categorias: ", e.getMessage());
			e.getStackTrace();
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional(readOnly = true)
	public ResponseEntity<CategoriaResponseRest> buscarPorId(Long id) {
		
		
		log.info("Inicio metodo buscarPorId");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		List<Categoria> list = new ArrayList<>();
		
		try {
			
			Optional<Categoria> categoria = categoriaDao.findById(id);
			
			if(categoria.isPresent()) {
				list.add(categoria.get());
				response.getCategoriaResponse().setCategoria(list);
			}else {
				log.error("Error en consultar categoria");
				response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		}catch(Exception e) {
			log.error("Error en consultar categoria");
			response.setMetadata("Respuesta nok", "-1", "Categoria no encontrada");
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND);
		}
		response.setMetadata("Respuesta ok", "00", "Respuesta exitosa");
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoriaResponseRest> crear(Categoria categoria) {
		

		log.info("Inicio metodo Crear categoria");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		List<Categoria> list = new ArrayList<>();
		
		try {
			
			Categoria categoriaGuardada = categoriaDao.save(categoria);
			
			if( categoriaGuardada != null) {
				list.add(categoriaGuardada);
				response.getCategoriaResponse().setCategoria(list);
			} else {
				log.error("Error en grabar categoria");
				response.setMetadata("Respuesta nok", "-1", "Categoria no creada");
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.BAD_REQUEST);
			
			}
			
		}catch(Exception e) {
			log.error("Error en crear categoria");
			response.setMetadata("Respuesta nok", "-1", "Error al grabar Categoria");
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND);
		}
		
		response.setMetadata("Respuesta ok", "00", "Categoria creada");
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
	}

	@Override
	@Transactional
	public ResponseEntity<CategoriaResponseRest> actualizar(Categoria categoria, Long id) {
		
		log.info("Inicio metodo actualizar");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		List<Categoria> list = new ArrayList<>();
		
		try {
			
			Optional<Categoria> categoriaBuscada = categoriaDao.findById(id);
			
			if ( categoriaBuscada.isPresent()) {
				
				categoriaBuscada.get().setNombre(categoria.getNombre());
				categoriaBuscada.get().setDescripcion(categoria.getDescripcion());
				
				Categoria categoriaActualizar = categoriaDao.save(categoriaBuscada.get()); //Aqui actualizamos
				
				if (categoriaActualizar!= null) {
					
					response.setMetadata("Respuesta ok", "00", "Categoria Actualizada");
					list.add(categoriaActualizar);
					response.getCategoriaResponse().setCategoria(list);
				}else {
					log.error("error en actualizar categoria");
					response.setMetadata("Respuesta nok", "-1", "Categoria no creada");
					return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.BAD_REQUEST);
				}
				
			} else {
				log.error("error en actualizar categoria");
				response.setMetadata("Respuesta nok", "-1", "Categoria no creada");
				return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.NOT_FOUND);
			}
			
		} catch(Exception e) {
			
			log.error("error en actualizar categoria", e.getMessage());
			e.getStackTrace();
			response.setMetadata("Respuesta nok", "-1", "Categoria no creada");
			return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		
		}
		
		
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
		
	}

	@Override
	@Transactional
	public ResponseEntity<CategoriaResponseRest> eliminar(Long id) {
		
		log.info("Inicio metodo eliminar categoria");
		
		CategoriaResponseRest response = new CategoriaResponseRest();
		
		try {
			
			//eliminar el registro
			
			categoriaDao.deleteById(id);
			response.setMetadata("Respuesta ok", "00", "Categoria Eliminada");
			
		}catch (Exception e) {
			log.error("error en eliminar categoria", e.getMessage());
		}
		return new ResponseEntity<CategoriaResponseRest>(response, HttpStatus.OK);
		
	}

	
	
}
