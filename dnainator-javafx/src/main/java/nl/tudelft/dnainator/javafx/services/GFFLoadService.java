package nl.tudelft.dnainator.javafx.services;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import nl.tudelft.dnainator.annotation.AnnotationCollection;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.utils.AppConfig;
import nl.tudelft.dnainator.parser.AnnotationParser;
import nl.tudelft.dnainator.parser.impl.GFF3AnnotationParser;

import java.io.IOException;

/**
 * This class enables mutation loading to be executed in the background.
 */
public class GFFLoadService extends Service<AnnotationCollection> {
	private StringProperty gffFilePath = new SimpleStringProperty(this, "gffFilePath");
	private ObjectProperty<Graph> graph = new SimpleObjectProperty<>(this, "graph");

	/**
	 * Creates a GFFLoadService and sets the file path to the stored path, if it exists.
	 */
	public GFFLoadService() {
		gffFilePath.set(AppConfig.getInstance().getGffPath());
	}

	/**
	 * Sets the GFF filename to the specified value.
	 * @param fileName The new filename.
	 */
	public final void setGffFilePath(String fileName) {
		gffFilePath.set(fileName);
	}

	/**
	 * @return The filename of the GFF file.
	 */
	public final String getGffFilePath() {
		return gffFilePath.get();
	}

	/**
	 * @return The GFF filename property.
	 */
	public StringProperty gffFilePathProperty() {
		return gffFilePath;
	}

	/**
	 * @return The database path. Needed for supporting multiple databases, and this property
	 * is bound to the database property in {@link GraphLoadService}.
	 */
	public ObjectProperty<Graph> graphProperty() {
		return graph;
	}

	@Override
	protected Task<AnnotationCollection> createTask() {
		return new Task<AnnotationCollection>() {
			@Override
			protected AnnotationCollection call() {
				AnnotationParser as;
				Graph g = graph.get();

				try {
					as = new GFF3AnnotationParser(gffFilePath.get());
					g.getAnnotations().addAnnotations(as);
					return g.getAnnotations();
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}
		};
	}

	@Override
	public void restart() {
		super.restart();
		AppConfig.getInstance().setGffPath(getGffFilePath());
	}
}
