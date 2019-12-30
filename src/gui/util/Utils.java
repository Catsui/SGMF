package gui.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Locale;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static <T> void formatTableColumnDate(TableColumn<T, Date> tableColumn, String format) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Date> cell = new TableCell<T, Date>() {
				private SimpleDateFormat sdf = new SimpleDateFormat(format);

				@Override
				protected void updateItem(Date item, boolean empty) {
					super.updateItem(item, empty);
					if (empty || item == null) {
						setText(null);
					} else {
						setText(sdf.format(item));
					}
				}
			};
			return cell;
		});
	}

	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}

	public static void formatDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}

	public static void enhanceDatePickers(DatePicker... datePickers) {
		for (DatePicker datePicker : datePickers) {
			datePicker.setConverter(new StringConverter<LocalDate>() {

				private final DateTimeFormatter fastFormatter1 = DateTimeFormatter.ofPattern("ddMMuuuu");
				private final DateTimeFormatter fastFormatter2 = DateTimeFormatter.ofPattern("d/M/u");
				private final DateTimeFormatter defaultFormatter = DateTimeFormatter.ofPattern("dd/MM/uuuu");

				@Override
				public String toString(LocalDate object) {
					return object.format(defaultFormatter);
				}

				@Override
				public LocalDate fromString(String string) {
					try {
						return LocalDate.parse(string, fastFormatter1);
					} catch (DateTimeParseException ignored) {
					}
					try {
						return LocalDate.parse(string, fastFormatter2);
					} catch (DateTimeParseException ignored) {
					}
					return LocalDate.parse(string, defaultFormatter);
				}
			});

			TextField textField = datePicker.getEditor();
			textField.addEventHandler(KeyEvent.KEY_TYPED, event -> {
				if (!"0123456789/".contains(event.getCharacter())) {
					return;
				}
				if ("/".equals(event.getCharacter()) && (textField.getText().isEmpty()
						|| textField.getText().charAt(textField.getCaretPosition() - 1) == '/')) {
					// If the users types slash again after it has been added, cancels it.
					System.out.println("Cancelando o bagulho!");
					event.consume();
				}
				textField.selectForward();
				if (!event.getCharacter().equals("/") && textField.getSelectedText().equals("/")) {
					textField.cut();
					textField.selectForward();
				}
				textField.cut();

				Platform.runLater(() -> {
					String textUntilHere = textField.getText(0, textField.getCaretPosition());
					if (textUntilHere.matches("\\d\\d") || textUntilHere.matches("\\d\\d/\\d\\d")) {
						String textAfterHere = "";
						try {
							textAfterHere = textField.getText(textField.getCaretPosition() + 1,
									textField.getText().length());
						} catch (Exception ignored) {
						}
						int caretPosition = textField.getCaretPosition();
						textField.setText(textUntilHere + "/" + textAfterHere);
						textField.positionCaret(caretPosition + 1);
					}
				});
			});
		}

	}
}