package placebooks.client.ui.dialogs;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import placebooks.client.AbstractCallback;
import placebooks.client.PlaceBookService;
import placebooks.client.model.LoginDetails;
import placebooks.client.model.Shelf;
import placebooks.client.model.User;
import placebooks.client.resources.Resources;
import placebooks.client.ui.elements.DatePrinter;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.Response;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.NoSelectionModel;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;

public class PlaceBookAccountsDialog extends Composite
{
	interface PlaceBookAccountsDialogUiBinder extends UiBinder<Widget, PlaceBookAccountsDialog>
	{
	}

	private static PlaceBookAccountsDialogUiBinder uiBinder = GWT.create(PlaceBookAccountsDialogUiBinder.class);

	private static final String[] SERVICES = { "Everytrail" };

	private static final ProvidesKey<LoginDetails> keyProvider = new ProvidesKey<LoginDetails>()
	{
		@Override
		public Object getKey(final LoginDetails details)
		{
			return details.getID();
		}
	};

	@UiField
	Panel tablePanel;

	@UiField
	Panel buttonPanel;
	
	private CellTable<LoginDetails> cellTable;
	private User user;

	public PlaceBookAccountsDialog(final User user)
	{
		this.user = user;
		initWidget(uiBinder.createAndBindUi(this));
		onInitialize();
		updateUser();		
	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns(final SelectionModel<LoginDetails> selectionModel)
	{
		// Service
		final Column<LoginDetails, String> serviceColumn = new Column<LoginDetails, String>(new TextCell())
		{
			@Override
			public String getValue(final LoginDetails object)
			{
				return object.getService();
			}
		};
		cellTable.addColumn(serviceColumn, "Service");

		// Username.
		final Column<LoginDetails, String> userNameColumn = new Column<LoginDetails, String>(new TextCell())
		{
			@Override
			public String getValue(final LoginDetails object)
			{
				return object.getUsername();
			}
		};
		cellTable.addColumn(userNameColumn, "Username");

		// Last Update
		final Column<LoginDetails, String> lastUpdateColumn = new Column<LoginDetails, String>(new TextCell())
		{
			@Override
			public String getValue(final LoginDetails object)
			{
				if (object.isSyncInProgress())
				{
					return "Sync In Progress";
				}
				else if(object.getLastSync() == 0)
				{
					return "Never Synced";
				}
				else
				{
					return "Last Synced: " + DatePrinter.formatDate(new Date((long) object.getLastSync()));
				}
			}
		};
		cellTable.addColumn(lastUpdateColumn, "Status");

		// Update
		final ActionCell<LoginDetails> updateCell = new ActionCell<LoginDetails>("Sync Now",
				new ActionCell.Delegate<LoginDetails>()
				{
					@Override
					public void execute(final LoginDetails details)
					{
						PlaceBookService.sync(details.getService(), new AbstractCallback()
						{
							@Override
							public void success(final Request request, final Response response)
							{
							}
						});
						details.setSyncInProgress(true);
						setUser(user);
					}
				});

		final Column<LoginDetails, LoginDetails> updateColumn = new Column<LoginDetails, LoginDetails>(updateCell)
		{
			@Override
			public LoginDetails getValue(final LoginDetails details)
			{
				return details;
			}
		};
		cellTable.addColumn(updateColumn);
	}

	private void onInitialize()
	{
		cellTable = new CellTable<LoginDetails>(keyProvider);
		cellTable.setWidth("100%", true);

		final SelectionModel<LoginDetails> selectionModel = new NoSelectionModel<LoginDetails>(keyProvider);
		cellTable.setSelectionModel(selectionModel);

		initTableColumns(selectionModel);

		tablePanel.add(cellTable);
		
		setUser(user);
	}
	
	private void setUser(final User user)
	{
		this.user = user;
		
		final List<String> serviceList = new ArrayList<String>();
		for (final String service : SERVICES)
		{
			serviceList.add(service);
		}
		
		
		boolean syncing = false;
		final List<LoginDetails> list = new ArrayList<LoginDetails>();
		for (final LoginDetails details : user.getLoginDetails())
		{
			if(details.isSyncInProgress())
			{
				syncing = true;
			}
			serviceList.remove(details.getService());
			list.add(details);
		}
		
		if(syncing)
		{
			new Timer()
			{
				
				@Override
				public void run()
				{
					updateUser();					
				}
			}.schedule(1000);
		}

		buttonPanel.clear();
		cellTable.setRowData(list);
	
		for (final String service : serviceList)
		{
			buttonPanel.add(new Button("Link " + service + " Account", new ClickHandler()
			{

				@Override
				public void onClick(final ClickEvent arg0)
				{
					final PopupPanel dialogBox = new PopupPanel();
					dialogBox.setGlassEnabled(true);
					dialogBox.setAnimationEnabled(true);
					final PlaceBookLoginDialog account = new PlaceBookLoginDialog("Link " + service + " Account", "Link Account", service
							+ " Username:");
					account.addClickHandler(new ClickHandler()
					{

						@Override
						public void onClick(final ClickEvent event)
						{
							dialogBox.getElement().getStyle().setCursor(Cursor.WAIT);
							account.setDisabled();
							PlaceBookService.linkAccount(	account.getUsername(), account.getPassword(), service,
															new AbstractCallback()
															{
																@Override
																public void failure(final Request request,
																		final Response response)
																{
																	account.setErrorText(service + " Login Failed");
																	dialogBox.getElement().getStyle().clearCursor();																	
																	account.setEnabled();																	
																}

																@Override
																public void success(final Request request,
																		final Response response)
																{
																	dialogBox.hide();
																	Shelf shelf = Shelf.parse(response.getText());
																	if(shelf != null)
																	{
																		setUser(shelf.getUser());
																	}
																}
															});
						}
					});
					dialogBox.add(account);
					dialogBox.setStyleName(Resources.INSTANCE.style().dialog());
					dialogBox.setGlassStyleName(Resources.INSTANCE.style().dialogGlass());
					dialogBox.setAutoHideEnabled(true);

					dialogBox.center();
					dialogBox.show();
				}
			}));
		}	
	}
	
	private void updateUser()
	{
		PlaceBookService.getShelf(new AbstractCallback()
		{
			@Override
			public void success(Request request, Response response)
			{
				Shelf shelf = Shelf.parse(response.getText());
				if(shelf != null)
				{
					setUser(shelf.getUser());
				}
			}
		});
	}
}