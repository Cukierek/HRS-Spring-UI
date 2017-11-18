package pl.com.bottega.hrs.ui;

import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;

public class EmployeeDetailsView extends UI {

	@Override
	protected void init(VaadinRequest vaadinRequest) {
		setContent(new Label("Details"));
	}
}
