package pubinterface.impl;

import pubinterface.HelloInterface;

public class HelloInterfaceImpl implements HelloInterface {

	@Override
	public String getResult(String msg) {

		return "hello rpc  " + msg;
	}

	@Override
	public String getAAA(String msg) {
		// TODO Auto-generated method stub
		return null;
	}

}
