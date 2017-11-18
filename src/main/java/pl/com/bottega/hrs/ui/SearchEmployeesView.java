package pl.com.bottega.hrs.ui;

import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.ui.NumberField;
import pl.com.bottega.hrs.application.BasicEmployeeDto;
import pl.com.bottega.hrs.application.EmployeeFinder;
import pl.com.bottega.hrs.application.EmployeeSearchCriteria;
import pl.com.bottega.hrs.application.EmployeeSearchResults;
import pl.com.bottega.hrs.ui.general.PagingComponent;
import pl.com.bottega.hrs.ui.general.UIConstants;

import java.util.List;

@SpringUI
public class SearchEmployeesView extends UI {

	@Autowired
	SpringViewProvider viewProvider;

	@Autowired
	private EmployeeFinder employeeFinder;

	private Layout rootLayout;

	private Grid<BasicEmployeeDto> employeesGrid;

	private Layout searchFieldsLayout;

	private TextField firstNameSearchTextField;
	private TextField lastNameSearchTextField;
	private DateField birthDateFromSearchDateField;
	private DateField birthDateToSearchDateField;
	private DateField hireDateFromSearchDateField;
	private DateField hireDateToSearchDateField;
	private NumberField salaryFromSearchTextField;
	private NumberField salaryToSearchTextField;
	private TextField titlesSearchTextField;
	private TextField departmentsSearchTextField;

	private Label totalResultsFoundLabel;

	private PagingComponent pagingComponent;

	private int previouslySearchedLastPageIndex;

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		createRootLayout();
		createAndConfigureChildComponents();
		populateRootLayout();
	}

	private void createAndConfigureChildComponents() {
		buildAndConfigureEmployeesGrid();
		buildAndConfigureSearchTextFields();
		buildAndConfigureAdditionalComponents();
	}

	private void buildAndConfigureAdditionalComponents() {
		totalResultsFoundLabel = new Label();
		pagingComponent = new PagingComponent();
		pagingComponent.setSizeFull();
		configurePagingFieldsAndButtons();
	}

	private void buildAndConfigureSearchTextFields() {
		createSearchTextFields();
		configureFilterFields();
		buildAndConfigureLayoutForSearchTextFields();
	}

	private void buildAndConfigureEmployeesGrid() {
		employeesGrid = new Grid<>();
		employeesGrid.addColumn(BasicEmployeeDto::getEmpNo).setCaption(UIConstants.EMPLOYEE_NUMBER);
		employeesGrid.addColumn(BasicEmployeeDto::getFirstName).setCaption(UIConstants.FIRSTNAME);
		employeesGrid.addColumn(BasicEmployeeDto::getLastName).setCaption(UIConstants.LASTNAME);
		employeesGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
		employeesGrid.addSelectionListener(event -> {
			if(event.getFirstSelectedItem().isPresent()) {
				BasicEmployeeDto bed = event.getFirstSelectedItem().get();
				// Notification.show(bed.toString());
			}
		});
		employeesGrid.setSizeFull();
	}

	private void buildAndConfigureLayoutForSearchTextFields() {
		searchFieldsLayout = new HorizontalLayout();
		searchFieldsLayout.addComponent(firstNameSearchTextField);
		searchFieldsLayout.addComponent(lastNameSearchTextField);
		searchFieldsLayout.addComponent(birthDateFromSearchDateField);
		searchFieldsLayout.addComponent(birthDateToSearchDateField);
		searchFieldsLayout.addComponent(hireDateFromSearchDateField);
		searchFieldsLayout.addComponent(hireDateToSearchDateField);
		searchFieldsLayout.addComponent(salaryFromSearchTextField);
		searchFieldsLayout.addComponent(salaryToSearchTextField);
	}

	private void populateRootLayout() {
		rootLayout.addComponent(searchFieldsLayout);
		rootLayout.addComponent(employeesGrid);
		rootLayout.addComponent(totalResultsFoundLabel);
		rootLayout.addComponent(pagingComponent);
	}

	private void createRootLayout() {
		rootLayout = new VerticalLayout();
		setContent(rootLayout);
	}

	private void searchEmployees() {
		EmployeeSearchCriteria esc = collectCriteria();
		EmployeeSearchResults esr = getSearchResults(esc);
		populateViewWithSearchResults(esr, esc.getPageNumber());
	}

	private void searchNextPageOfEmployees() {
		EmployeeSearchCriteria esc = collectCriteria();
		esc.setPageNumber(esc.getPageNumber() + 1);
		if(esc.getPageNumber() >= previouslySearchedLastPageIndex) return;
		EmployeeSearchResults esr = getSearchResults(esc);
		populateViewWithSearchResults(esr, esc.getPageNumber());
	}

	private void searchPreviousPageOfEmployees() {
		EmployeeSearchCriteria esc = collectCriteria();
		esc.setPageNumber(esc.getPageNumber() - 1);
		if(esc.getPageNumber() < 1) return;
		EmployeeSearchResults esr = getSearchResults(esc);
		populateViewWithSearchResults(esr, esc.getPageNumber());
	}

	private void searchFirstPageOfEmployees() {
		EmployeeSearchCriteria esc = collectCriteria();
		if(esc.getPageNumber() <= 1) return;
		esc.setPageNumber(1);
		EmployeeSearchResults esr = getSearchResults(esc);
		populateViewWithSearchResults(esr, esc.getPageNumber());
	}

	private void searchLastPageOfEmployees() {
		EmployeeSearchCriteria esc = collectCriteria();
		if(esc.getPageNumber() >= previouslySearchedLastPageIndex) return;
		EmployeeSearchResults esr = getSearchResults(esc);
		esc.setPageNumber(esr.getPagesCount());
		esr = getSearchResults(esc);
		populateViewWithSearchResults(esr, esc.getPageNumber());
	}

	private void populateViewWithSearchResults(EmployeeSearchResults esr, int currentPageValue) throws NumberFormatException {
		List<BasicEmployeeDto> foundEmployees = esr.getResults();
		employeesGrid.setItems(foundEmployees);
		if (esr.getPagesCount() <= 0) pagingComponent.setDefaultValueForPagesCount();
		else pagingComponent.setValueForPagesCountLabel(esr.getPagesCount());
		previouslySearchedLastPageIndex = esr.getPagesCount();
		pagingComponent.setCurrentPageValue(currentPageValue);
		totalResultsFoundLabel.setValue(String.format(UIConstants.EMPLOYEES_FOUND_CAPTION, esr.getTotalCount()));
	}

	private EmployeeSearchResults getSearchResults(EmployeeSearchCriteria emc) {
		return employeeFinder.search(emc);
	}

	private EmployeeSearchCriteria collectCriteria() {
		EmployeeSearchCriteria emc = new EmployeeSearchCriteria();

		if (!firstNameSearchTextField.isEmpty())
			emc.setFirstNameQuery(firstNameSearchTextField.getValue());
		if (!lastNameSearchTextField.isEmpty())
			emc.setLastNameQuery(lastNameSearchTextField.getValue());
		if (!birthDateFromSearchDateField.isEmpty())
			emc.setBirthDateFrom(birthDateFromSearchDateField.getValue());
		if (!birthDateToSearchDateField.isEmpty())
			emc.setBirthDateTo(birthDateToSearchDateField.getValue());
		if (!hireDateFromSearchDateField.isEmpty())
			emc.setBirthDateFrom(hireDateFromSearchDateField.getValue());
		if (!hireDateToSearchDateField.isEmpty())
			emc.setBirthDateTo(hireDateToSearchDateField.getValue());
		if (!salaryFromSearchTextField.isEmpty())
			emc.setSalaryFrom((Double.valueOf(salaryFromSearchTextField.getDoubleValueDoNotThrow())).intValue());
		if (!salaryToSearchTextField.isEmpty())
			emc.setSalaryTo((Double.valueOf(salaryToSearchTextField.getDoubleValueDoNotThrow())).intValue());

		emc.setPageSize(pagingComponent.getPageSize());
		emc.setPageNumber(pagingComponent.getPageNumber());
		return emc;
	}

	private void createSearchTextFields() {
		firstNameSearchTextField = new TextField(UIConstants.FIRSTNAME);
		lastNameSearchTextField = new TextField(UIConstants.LASTNAME);
		birthDateFromSearchDateField = new DateField(UIConstants.BIRTHDATE_FROM);
		birthDateToSearchDateField = new DateField(UIConstants.BIRTHDATE_TO);
		hireDateFromSearchDateField = new DateField(UIConstants.HIREDATE_FROM);
		hireDateToSearchDateField = new DateField(UIConstants.HIREDATE_TO);
		salaryFromSearchTextField = new NumberField(UIConstants.SALARY_FROM);
		salaryToSearchTextField = new NumberField(UIConstants.SALARY_TO);
	}

	private void configureFilterFields() {
		firstNameSearchTextField.addValueChangeListener(event -> searchEmployees());
		lastNameSearchTextField.addValueChangeListener(event -> searchEmployees());
		birthDateFromSearchDateField.addValueChangeListener(event -> searchEmployees());
		birthDateToSearchDateField.addValueChangeListener(event -> searchEmployees());
		hireDateFromSearchDateField.addValueChangeListener(event -> searchEmployees());
		hireDateToSearchDateField.addValueChangeListener(event -> searchEmployees());
		salaryFromSearchTextField.addValueChangeListener(event -> searchEmployees());
		salaryToSearchTextField.addValueChangeListener(event -> searchEmployees());
	}

	private void configurePagingFieldsAndButtons() {
		pagingComponent.getCurrentPageTextField().addValueChangeListener(valueChangeEvent -> searchEmployees());
		pagingComponent.getNextPageButton().addClickListener(clickEvent -> searchNextPageOfEmployees());
		pagingComponent.getPreviousPageButton().addClickListener(clickEvent -> searchPreviousPageOfEmployees());
		pagingComponent.getFirstPageButton().addClickListener(clickEvent -> searchFirstPageOfEmployees());
		pagingComponent.getLastPageButton().addClickListener(clickEvent -> searchLastPageOfEmployees());
		pagingComponent.getResultsPerPageComboBox().addValueChangeListener(valueChangeEvent -> searchEmployees());
	}
}
