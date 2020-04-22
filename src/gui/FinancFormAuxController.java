package gui;

import java.time.ZoneId;
import java.util.Date;

public class FinancFormAuxController extends FinancFormController {

	@Override
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entidade nula.");
		}
		txtId.setText(entity.getId() == null ? "" : String.valueOf(entity.getId()));
		txtNome.setText(entity.getNome());
		txtTelefone.setText(entity.getTelefone());
		if (entity.getPagamento() != null) {
			java.util.Date pagamento = new Date(entity.getPagamento().getTime());
			dpPagamento.setValue(pagamento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		if (entity.getReferencia() != null) {
			java.util.Date referencia = new Date(entity.getReferencia().getTime());
			dpReferencia.setValue(referencia.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		if (entity.getVencimento() != null) {
			java.util.Date vencimento = new Date(entity.getVencimento().getTime());
			dpVencimento.setValue(vencimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		}
		txtMensalidade.setText(entity.getMensalidade() == null ? "" : String.valueOf(entity.getMensalidade()));
		txtObserv.setText(entity.getObserv());
	}
}
