/**
 * 
 */
package com.wrangler.ui.fd;

import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalSplitPanel;
import com.wrangler.fd.FDDetector;
import com.wrangler.fd.FDFactory;
import com.wrangler.fd.FunctionalDependency;
import com.wrangler.fd.HardFD;
import com.wrangler.fd.SoftFD;
import com.wrangler.load.Attribute;
import com.wrangler.load.Database;
import com.wrangler.load.Relation;
import com.wrangler.load.RelationFactory;

/**
 * @author edenzik
 *
 */
class FDAddRemovePanel extends VerticalSplitPanel {

	/**
	 * 
	 */
	FDAddRemovePanel(final FDTable fdTable, final TableSelection tableSelection, final Database db, final UI ui) {
		initLayout();
		Button removeFD = new Button("Remove FD");
		removeFD.setSizeFull();
		removeFD.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				fdTable.removeSelectedValue();
			}
		});

		Button addFD = new Button("Add FD");
		
		
		final ComboBox selectFrom = new ComboBox();
		selectFrom.setInputPrompt("From");
		
		final ComboBox selectTo = new ComboBox();
		selectTo.setInputPrompt("To");
		HorizontalSplitPanel attSelection = new HorizontalSplitPanel(selectFrom, selectTo);
		attSelection.setSizeFull();
		attSelection.setLocked(true);
		attSelection.setSplitPosition(50, Unit.PERCENTAGE);
		HorizontalSplitPanel attSelectionAndButton = new HorizontalSplitPanel(attSelection, addFD);
		attSelectionAndButton.setSizeFull();
		
		selectFrom.setSizeFull();
		selectTo.setSizeFull();
		addFD.setSizeFull();
		
		//VerticalSplitPanel addFDSplitPanel = new VerticalSplitPanel(FDSelectionLayout, addFD);
		tableSelection.addValueChangeListener(new ComboBox.ValueChangeListener() {
			@Override
			public void valueChange(ValueChangeEvent event) {
				if (event.getProperty().getValue()!=null){
					selectFrom.removeAllItems();
					selectTo.removeAllItems();
					for (Attribute att : db.getDbHelper().getRelationAttributes(RelationFactory.createExistingRelation(event.getProperty().getValue().toString(), db))){
						selectFrom.addItem(att);
						selectTo.addItem(att);
					}
				}

			}
		});
		
		addComponent(attSelectionAndButton);
		addComponent(removeFD);
		addFD.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				
				if (selectFrom.getValue()!=null && selectTo.getValue() != null){
					//fdTable.getFdSet().keySet()
					
					//((Attribute) selectFrom.getValue());
					ui.addWindow(new FDViolationWindow(FDFactory.createSoftFD((Attribute) selectFrom.getValue(), (Attribute) selectTo.getValue())));
					//fdTable.insert(new HardFD((Attribute) selectFrom.getValue(), (Attribute) selectTo.getValue()));
				}
			}
		});
		
	}
	
	private void initLayout(){
		setSizeFull();
		setSplitPosition(50, Unit.PERCENTAGE);
		setLocked(true);
	}

}
