package pl.com.bottega.hrs.ui.general;

import com.vaadin.shared.ui.ValueChangeMode;
import com.vaadin.ui.*;

import java.util.ArrayList;
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
		resultsPerPageLabel = new Label(UIConstants.RESULTS_IN_TABLE_CAPTION);
		resultsPerPageLabel.setSizeUndefined();
		resultsPerPageComboBox = new ComboBox<>();
		currentPageLabel = new Label(UIConstants.PAGE_LABEL);
		currentPageTextField = new TextField();
		currentPageTextField.setValue("1");
		lastPageLabel = new Label(PAGE_COUNT_LABEL_DEFAULT_VALUE);

		lastPageLabel.setWidth(4, Unit.EM);
		currentPageTextField.setWidth(5, Unit.EM);
		currentPageTextField.setValueChangeMode(ValueChangeMode.BLUR);

		firstPageButton = new Button(UIConstants.FIRST);
		lastPageButton = new Button(UIConstants.LAST);
		previousPageButton = new Button(UIConstants.PREVIOUS);
		nextPageButton = new Button(UIConstants.NEXT);

		List<Integer> resultsPerPageOptions = createItemsForPerPageComboBox();

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
		comboLayout.setComponentAlignment(resultsPerPageLabel, Alignment.MIDDLE_LEFT);

		HorizontalLayout rootLayout = new HorizontalLayout(comboLayout, pagingLayout);
		rootLayout.setSizeFull();
		rootLayout.setComponentAlignment(comboLayout, Alignment.MIDDLE_LEFT);
		rootLayout.setComponentAlignment(pagingLayout, Alignment.MIDDLE_RIGHT);

		setCompositionRoot(rootLayout);
	}

	private List<Integer> createItemsForPerPageComboBox(){
		List<Integer> items = new ArrayList<>(5);
		items.add(5);
		items.add(10);
		items.add(20);
		items.add(50);
		items.add(100);
		return items;
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
