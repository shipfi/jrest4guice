package org.jrest.test.service;

import java.rmi.RemoteException;
import java.util.List;

import org.jrest.test.service.entity.Contact;

public interface ContactService {
	public String createContact(Contact contact) throws RemoteException;
	
	public List<Contact> listContacts(int first,int max) throws RemoteException;
	
	public Contact findContactById(String contactId) throws RemoteException;
	
	public void updateContact(Contact contact) throws RemoteException;
	public void deleteContact(String contactId) throws RemoteException;
}
