package persistence;

import business.CFD;
import business.Long;
import business.Short;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

public class CFDNegociadorDAO implements List<CFD>{
	private Integer nifNegociador;

	public CFDNegociadorDAO(int nif) {
		this.nifNegociador = nif;
	}

	@Override
	public int size() {
		Connection c = Connect.connect();
		if (c == null) {
			return 0;
		}
		Statement s;
		int result = 0;
		try {
			s = c.createStatement();
			ResultSet rs = s.executeQuery("select count(*) from cfd where nifnegociador = " + nifNegociador +" and aberto = true");
			rs.next();
			result = rs.getInt(1);
			rs.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		Connect.close(c);

		return result;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<CFD> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T> T[] toArray(T[] ts) {
		return null;
	}

	@Override
	public boolean add(CFD cfd) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends CFD> collection) {
		return false;
	}

	@Override
	public boolean addAll(int i, Collection<? extends CFD> collection) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		return false;
	}

	@Override
	public void clear() {

	}

	@Override
	public CFD get(int i) {
		Connection c = Connect.connect();
		if (c == null) {
			return null;
		}

		PreparedStatement s;
		try {
			s = c.prepareStatement("select * from cfd where nifnegociador = ? and aberto = true");
			s.setInt(1,this.nifNegociador);

			ResultSet resultSet = s.executeQuery();
			if (!resultSet.isBeforeFirst())
				return null;


			while(i >= 0) {
				resultSet.next();
				i--;
			}
			int id = resultSet.getInt("id");
			LocalDateTime data = resultSet.getTimestamp("data").toLocalDateTime();
			double udativo = resultSet.getDouble("unidadesdeativo");
			double vpuc = resultSet.getDouble("valorporunidadenacompra");
			Double limitesup = resultSet.getDouble("limitesup");
			Double limiteinf = resultSet.getDouble("limiteinf");
			String idAtivo = resultSet.getString("idativo");
			int nif = resultSet.getInt("nifnegociador");
			boolean aberto = resultSet.getBoolean("aberto");
			boolean isLong = resultSet.getBoolean("long");

			CFD n;
			if (isLong)
				n = new Long(id, data, udativo, vpuc, limiteinf, limitesup, idAtivo, nif, aberto);
			else
				n = new Short(id, data, udativo, vpuc, limiteinf, limitesup, idAtivo, nif, aberto);

			resultSet.close();

			Connect.close(c);
			return n;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		Connect.close(c);

		return null;
	}

	@Override
	public CFD set(int i, CFD cfd) {
		return null;
	}

	@Override
	public void add(int i, CFD cfd) {

	}

	@Override
	public CFD remove(int i) {
		return null;
	}

	@Override
	public int indexOf(Object o) {
		return 0;
	}

	@Override
	public int lastIndexOf(Object o) {
		return 0;
	}

	@Override
	public ListIterator<CFD> listIterator() {
		return null;
	}

	@Override
	public ListIterator<CFD> listIterator(int i) {
		return null;
	}

	@Override
	public List<CFD> subList(int i, int i1) {
		return null;
	}
}