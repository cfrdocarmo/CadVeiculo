package cfrdocarmo.veiculo.domain.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import cfrdocarmo.veiculo.domain.model.Veiculo;
import cfrdocarmo.veiculo.domain.model.exception.EntidadeNaoEncontradaException;
import cfrdocarmo.veiculo.domain.model.repository.VeiculoRepository;

@Service
public class CadastroVeiculoService {

	private static final String MSG_VEICULO_NAO_ENCOTRADO = "Não existe um veículo com código %d";
	
	@Autowired
	private VeiculoRepository veiculoRepository;
	
	public Veiculo salvar(Veiculo veiculo) {
		return veiculoRepository.save(veiculo);
	}
	
	public void excluir(Long veiculoId) {
		try {
			veiculoRepository.deleteById(veiculoId);
		} catch(EmptyResultDataAccessException ex) {
			throw new EntidadeNaoEncontradaException(String.format(MSG_VEICULO_NAO_ENCOTRADO, veiculoId));
		}
	}
	
	public Veiculo buscarOuFalhar(Long veiculoId) {
		return veiculoRepository.findById(veiculoId)
			.orElseThrow(() -> new EntidadeNaoEncontradaException(
					String.format(MSG_VEICULO_NAO_ENCOTRADO, veiculoId)));
	}
}
