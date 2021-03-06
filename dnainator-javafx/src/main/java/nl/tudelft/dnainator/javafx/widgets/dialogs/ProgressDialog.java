package nl.tudelft.dnainator.javafx.widgets.dialogs;

import javafx.concurrent.Service;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ProgressBar;
import javafx.stage.Window;

/**
 * Creates an {@link Alert} while a file is loading.
 */
public class ProgressDialog extends Alert {
	private static final int PROGRESSBAR_WIDTH = 300;
	private static final String STYLE = "/style.css";
	private Window window;
	private ProgressBar progressBar;

	/**
	 * Sets up the {@link Alert}, using the {@link Service} provided.
	 * When the service has succeeded, the alert is closed.
	 * @param window The parent Node of this dialog.
	 * @param service The service being watched.
	 */
	public ProgressDialog(Window window, Service<?> service) {
		super(AlertType.NONE);
		this.window = window;
		this.setOnCloseRequest(e -> service.cancel());
		setupProgressBar();
		setupAlert();
	}

	private void setupAlert() {
		this.setTitle("DNAinator");
		this.setHeaderText("Loading...");
		this.getButtonTypes().add(ButtonType.CANCEL);
		this.getDialogPane().getStylesheets().add(getClass().getResource(STYLE).toString());
		this.initOwner(window);
		this.getDialogPane().setContent(progressBar);
	}

	private void setupProgressBar() {
		progressBar = new ProgressBar();
		progressBar.setPrefWidth(PROGRESSBAR_WIDTH);
	}

	/**
	 * Set the progress of this dialog.
	 * @param progress	the progress
	 */
	public void setProgress(double progress) {
		progressBar.setProgress(progress);
	}
}
