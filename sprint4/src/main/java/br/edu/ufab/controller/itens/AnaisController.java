package br.edu.ufab.controller.itens;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import br.edu.ufab.exception.Exception;
import br.edu.ufab.model.entities.Autor;
import br.edu.ufab.model.entities.itens.Anais;
import br.edu.ufab.model.repositories.AutorRepository;
import br.edu.ufab.model.repositories.itens.AnaisRepository;
import br.edu.ufab.propertyeditors.AutorPropertyEditor;
/**
 * Classe responsavel por responder as requisições feitas para /anais
 * 
 * @author EquipeACL
 * 
 *
 * */
@RestController
@RequestMapping("/anais")
public class AnaisController {

	@Autowired private AutorPropertyEditor autorPropertyEditor;
	@Autowired private AutorRepository autorRepository;
	@Autowired private AnaisRepository anaisRepository;
	
	@GetMapping
	public List<Anais> listar() {
		return (List<Anais>) anaisRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Optional<Anais>> buscar(@PathVariable long id) {
		Optional<Anais> anais = anaisRepository.findById(id);
		if(anais == null){
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(anais);
	}
	
	@PostMapping
	public @ResponseBody ResponseEntity<Anais> salvar(@Valid @RequestBody Anais anais, BindingResult bindingResult) {
		
		if ( bindingResult.hasErrors() ) {
			throw new Exception();
		} else {
			Anais retorno = anaisRepository.save(anais);
			return ResponseEntity.ok(retorno);
		}
		
	}
	
	@DeleteMapping("/{id}")
	public @ResponseBody ResponseEntity<Void> deletar(@PathVariable long id) {
		Optional<Anais> anais = anaisRepository.findById(id);
		if(anais == null){
			return ResponseEntity.notFound().build();
		}
		anaisRepository.deleteById(id);
		return ResponseEntity.noContent().build();
		
	}
	
	@PutMapping("/{id}")
	public @ResponseBody ResponseEntity<Anais> atualizar(@PathVariable long id,@Valid @RequestBody Anais anais) {
		Optional<Anais> existente = anaisRepository.findById(id);
		if(existente == null){
			return ResponseEntity.notFound().build();
		}
		BeanUtils.copyProperties(anais,existente,"id");
		BeanUtils.copyProperties(existente,anais);
		anais = anaisRepository.save(anais);
		return ResponseEntity.ok(anais);
	}
	
	@InitBinder
	public void initBinder(WebDataBinder webDataBinder){
		webDataBinder.registerCustomEditor(Autor.class, autorPropertyEditor);
	}	
}
