package nl.tudelft.dnainator.javafx.widgets;

import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import nl.tudelft.dnainator.graph.Graph;
import nl.tudelft.dnainator.javafx.drawables.strains.Strain;
import nl.tudelft.dnainator.javafx.views.StrainView;

/**
 * The minimap gives a global overview of the DNA strains in the {@link StrainView}, based on the
 * amount of ranks.
 */
public class Minimap extends Pane {
	private static final String VIEW_STYLE = "view";
	private static final String SPACER_STYLE = "spacer";
	private static final double HEIGHT = 50;
	private static final double SPACER_HEIGHT = 7;
	private Strain strain;
	private Graph graph;
	private StrainView strainView;
	private DoubleProperty widthPerRank = new SimpleDoubleProperty(0, "widthPerRank");

	/**
	 * Instantiates a new {@link Minimap}.
	 * @param strain The {@link Strain} to operate on.
	 * @param graph The {@link Graph} that the {@link Strain} operates on.
	 * @param strainView The {@link StrainView} that contains this minimap.
	 */
	public Minimap(Strain strain, Graph graph, StrainView strainView) {
		this.strain = strain;
		this.graph = graph;
		this.strainView = strainView;
		this.widthPerRank.bind(widthProperty().divide(graph.getMaxRank()));
		setHeight(HEIGHT);

		drawSpacer();
		// Wait for the Strain to update its properties.
		Platform.runLater(this::drawViewport);

		setOnMouseClicked(e -> onMouseClicked(e));
	}

	private void drawSpacer() {
		Rectangle spacer = new Rectangle();
		spacer.getStyleClass().add(SPACER_STYLE);
		spacer.widthProperty().bind(widthPerRank.multiply(graph.getMaxRank()));
		spacer.setHeight(SPACER_HEIGHT);
		spacer.yProperty().bind(
				heightProperty().divide(2).subtract(spacer.heightProperty().divide(2)));
		getChildren().add(spacer);
	}

	private void drawViewport() {
		Rectangle view = new Rectangle();
		view.getStyleClass().add(VIEW_STYLE);
		view.xProperty().bind(strain.minRankProperty().multiply(widthPerRank));
		view.heightProperty().bind(heightProperty());
		view.widthProperty().bind(widthPerRank.multiply(
				strain.maxRankProperty().subtract(strain.minRankProperty())));
		getChildren().add(view);
	}

	private void onMouseClicked(MouseEvent e) {
		int rank = (int) (e.getX() / widthPerRank.get());
		strainView.gotoRank(rank);
	}
}
