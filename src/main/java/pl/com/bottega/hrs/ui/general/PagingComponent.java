package pl.com.bottega.hrs.ui.general;

import com.vaadin.data.HasValue;
import com.vaadin.server.UserError;
import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.xml.stream.events.Characters;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PagingComponent extends CustomComponent {

	private final String PAGE_COUNT_LABEL_DEFAULT_VALUE = String.format("/ %s", "?");

	private Label resultsPerPageLabel;
	private ComboBox<Integer> resultsPerPageComboBox;

	private Label currentPageLabel;
	private TextField currentPageTextField;
	private Label lastPageLabel;

	private Button firstPageButton;
	private Button lastPageButton;
	private Button previousPageButton;
	private Button nextPageButton;

	public PagingComponent() {
		resultsPerPageLabel = new Label("Wyników w tabeli");
		resultsPerPageLabel.setSizeUndefined();
		resultsPerPageComboBox = new ComboBox<>();
		currentPageLabel = new Label("Strona:");
		currentPageTextField = new TextField();
		currentPageTextField.setValue("1");
		lastPageLabel = new Label(PAGE_COUNT_LABEL_DEFAULT_VALUE);

		lastPageLabel.setWidth(4, Unit.EM);
		currentPageTextField.setWidth(5, Unit.EM);
		currentPageTextField.setValueChangeMode(ValueChangeMode.BLUR);

		firstPageButton = new Button("Pierwszy");
		lastPageButton = new Button("Ostatni");
		previousPageButton = new Button("Poprzedni");
		nextPageButton = new Button("Następny");

		List<Integer> resultsPerPageOptions = new ArrayList<>();
		resultsPerPageOptions.add(5);
		resultsPerPageOptions.add(10);
		resultsPerPageOptions.add(20);
		resultsPerPageOptions.add(50);
		resultsPerPageOptions.add(100);
		resultsPerPageComboBox.setItems(resultsPerPageOptions);
		resultsPerPageComboBox.setEmptySelectionAllowed(false);
		resultsPerPageComboBox.setSelectedItem(resultsPerPageOptions.get(2));
		resultsPerPageComboBox.setWidth(5, Unit.EM);
		resultsPerPageComboBox.addValueChangeListener(valueChangeEvent -> currentPageTextField.setValue("1"));

		HorizontalLayout pagingLayout = new HorizontalLayout(firstPageButton, previousPageButton, currentPageLabel,
				currentPageTextField, lastPageLabel, nextPageButton, lastPageButton);
		pagingLayout.setComponentAlignment(currentPageLabel, Alignment.MIDDLE_CENTER);
		pagingLayout.setComponentAlignment(lastPageLabel,Alignment.MIDDLE_CENTER);

		HorizontalLayout comboLayout = new HorizontalLayout(resultsPerPageLabel, resultsPerPageComboBox);
		comboLayout.setComponentAlignment(resultsPerPageLabel, Alignment.MIDDLE_CENTER);

		HorizontalLayout rootLayout = new HorizontalLayout(comboLayout, pagingLayout);
		// rootLayout.setComponentAlignment(pagingLayout, Alignment.MIDDLE_RIGHT);
		rootLayout.setSizeFull();

		setCompositionRoot(rootLayout);
	}

	public int getPageSize() {
		return resultsPerPageComboBox.getValue();
	}

	public int getPageNumber() {
		String selectedPageText = currentPageTextField.getValue();

		Integer value = Integer.parseInt(selectedPageText);

		// simple validation
		char[] ca = selectedPageText.toCharArray();
		for(char c : ca){
			if(!Character.isDigit(c)) value = 1;
		}

		if (value < 1) value = 1;
		return value;
	}

	public TextField getCurrentPageTextField() {
		return currentPageTextField;
	}

	public ComboBox<Integer> getResultsPerPageComboBox() {
		return resultsPerPageComboBox;
	}

	public void setValueForPagesCountLabel(Integer value) {
		lastPageLabel.setValue(String.format("/ %d", value));
	}

	public void setDefaultValueForPagesCount() {
		lastPageLabel.setValue(PAGE_COUNT_LABEL_DEFAULT_VALUE);
	}

	public Integer getCurrentPageValue() {
		return Integer.parseInt(currentPageTextField.getValue());
	}

	public void setCurrentPageValue(Integer pagesCount) {
		currentPageTextField.setValue(pagesCount.toString());
	}

	public Button getNextPageButton() {
		return nextPageButton;
	}

	public Button getFirstPageButton() {
		return firstPageButton;
	}

	public Button getLastPageButton() {
		return lastPageButton;
	}

	public Button getPreviousPageButton() {
		return previousPageButton;
	}
}
