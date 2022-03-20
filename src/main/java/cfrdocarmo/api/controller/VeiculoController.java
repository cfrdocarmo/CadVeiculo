package cfrdocarmo.api.controller;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import cfrdocarmo.veiculo.domain.model.MarcaFabricante;
import cfrdocarmo.veiculo.domain.model.Veiculo;
import cfrdocarmo.veiculo.domain.model.exception.EntidadeNaoEncontradaException;
import cfrdocarmo.veiculo.domain.model.repository.VeiculoRepository;
import cfrdocarmo.veiculo.domain.model.service.CadastroVeiculoService;

@RestController
@RequestMapping(value = "/veiculos")
public class VeiculoController {

	@Autowired
	private VeiculoRepository veiculoRepository;
	
	@Autowired
	private CadastroVeiculoService cadastroVeiculo;
	
	@GetMapping()
	public List<Veiculo> listar() {
		return veiculoRepository.findAll();
	}
	
	@GetMapping(value = "/{veiculoId}" )
	public Veiculo buscarPorId(@PathVariable Long veiculoId) {
		return cadastroVeiculo.buscarOuFalhar(veiculoId);
	}
	
	@GetMapping(value = "/porMarcaAno" )
	public List<Veiculo> buscarPorMarcaAno(MarcaFabricante marca, Integer ano) {
		return veiculoRepository.findByMarcaAndAno(marca, ano);
	}
	
	@GetMapping(value = "/porMarcaOuAno" )
	public List<Veiculo> buscarPorMarcaOrAno(MarcaFabricante marca, Integer ano) {
		return veiculoRepository.findByMarcaOrAno(marca, ano);
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Veiculo adicionar(@RequestBody Veiculo veiculo) {
		return cadastroVeiculo.salvar(veiculo);
	}

	@PutMapping("/{veiculoId}")
	public ResponseEntity<?> atualizar(@PathVariable Long veiculoId, @RequestBody Veiculo veiculo){
		try {
			Veiculo veiculoAtual = veiculoRepository.findById(veiculoId).orElse(null);
			
			if (veiculoAtual != null) {
				BeanUtils.copyProperties(veiculo, veiculoAtual, "id", "created");
				veiculoAtual = cadastroVeiculo.salvar(veiculoAtual);
				return ResponseEntity.ok(veiculoAtual);
		} else {
			return ResponseEntity.notFound().build();
		}
		}catch (EntidadeNaoEncontradaException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
	
	@PatchMapping("/{veiculoId}")
	public ResponseEntity<?> atualizarParcial(@PathVariable Long veiculoId, @RequestBody Map<String, Object> campos){
		
		Veiculo veiculoAtual = veiculoRepository.findById(veiculoId).orElse(null);
		
		if (veiculoAtual == null) {
			return ResponseEntity.notFound().build();
		}
		
		merge(campos, veiculoAtual);
		
		return atualizar(veiculoId, veiculoAtual);
	}

	private void merge(Map<String, Object> dadosOrigem, Veiculo veiculoDestino) {

		ObjectMapper objectMapper = new ObjectMapper();
		Veiculo veiculoOrigem = objectMapper.convertValue(dadosOrigem, Veiculo.class);
		
		dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
			Field field = ReflectionUtils.findField(Veiculo.class, nomePropriedade);
			field.setAccessible(true);
			
			Object novoValor = ReflectionUtils.getField(field, veiculoOrigem);
			
			System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);
			
			ReflectionUtils.setField(field, veiculoDestino, novoValor);
		});
	}
	
	@DeleteMapping(value = "/{veiculoId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void remover(@PathVariable Long veiculoId){
			cadastroVeiculo.excluir(veiculoId);
	}
	
}
