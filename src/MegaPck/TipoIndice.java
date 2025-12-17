package MegaPck;

public enum TipoIndice {
	TOPO, CENTRO, BASE;

///  0     1     2    	
	public static int count = TipoIndice.values().length;

	public int getIndex(TipoIndice tipo) {
		if (tipo == TipoIndice.TOPO) {
			return 0;
		} else if (tipo == TipoIndice.CENTRO) {
			return 1;
		} else {
			return 2;
		}
	}
}
