package cfrdocarmo.veiculo.domain.model.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class EntidadeEmUsoExceotion extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EntidadeEmUsoExceotion(String mensagem) {
		super(mensagem);
	}
}
