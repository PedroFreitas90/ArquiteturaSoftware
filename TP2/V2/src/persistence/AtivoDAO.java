package persistence;

import business.ativos.*;

import java.sql.*;
import java.util.*;

public class AtivoDAO implements Map<String,Ativo>{

	@Override
	public int size() {
		Connection c = Connect.connect();
		if (c == null){
			return 0;
		}
		Statement s;
		int result = 0;
		try {
			s = c.createStatement();
			ResultSet resultSet = s.executeQuery("select count(*) from ativo;");
			resultSet.next();
			result = resultSet.getInt(1);
			resultSet.close();

		} catch (SQLException e) {
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
	public boolean containsKey(Object o) {
		Connection c = Connect.connect();
		if (c == null){
			return false;
		}
		if (!(o instanceof String))
			return false;
		String key = (String) o;
		PreparedStatement s;
		boolean result = false;
		try {
			s = c.prepareStatement("select id from ativo where id = ?");
			s.setString(1,key);

			ResultSet resultSet = s.executeQuery();
			result = resultSet.isBeforeFirst();
			resultSet.close();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		Connect.close(c);

		return result;
	}

	@Override
	public boolean containsValue(Object o) {
		return false;
	}


	@Override
	public Ativo get(Object o) {

		Connection c = Connect.connect();
		if (c == null) {
			return null;
		}

		if (!(o instanceof String))
			return null;

		String key = (String) o;

		Ativo ativo = null;

		PreparedStatement s;
		try {
			s = c.prepareStatement("select * from ativo where id = ?");
			s.setString(1,key);

			ResultSet resultSet = s.executeQuery();
			if (!resultSet.isBeforeFirst())
				return null;

			resultSet.next();
			String id = resultSet.getString("id");
			String nome = resultSet.getString("nome");
			double valorPorUnidade = resultSet.getDouble("valorporunidade");


			String classeAtivo = DAOHelper.getClassAtivo(c,key);

			if (classeAtivo.equals("acao")) {
                return DAOHelper.getAcao(c,id, nome, valorPorUnidade);
            }

			else if (classeAtivo.equals("moeda"))
                ativo = DAOHelper.getMoeda(c,id, nome, valorPorUnidade);

			else if (classeAtivo.equals("indice"))
                ativo = DAOHelper.getIndice(c,id, nome, valorPorUnidade);

			else if (classeAtivo.equals("commodity"))
                ativo = DAOHelper.getCommodity(c,id, nome, valorPorUnidade);

			resultSet.close();

		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		Connect.close(c);

		return ativo;
	}




	@Override
	public Ativo put(String s, Ativo ativo) {

		Connection c = Connect.connect();
		if (c == null) {
			return null;
		}

		if (!s.equals(ativo.getId()))
			return null;

		PreparedStatement st;
		try {

			if (this.containsKey(s)) {
				st = c.prepareStatement("update ativo set id = ?, nome = ?, valorporunidade = ? where id = ?;");
				st.setString(4,ativo.getId());

				st.setString(1,ativo.getId());
				st.setString(2,ativo.getNome());
				st.setDouble(3,ativo.getValorPorUnidade());

				st.executeUpdate();

			}
			else {
				st = c.prepareStatement("insert into ativo values (?, ?, ?);");


				st.setString(1, ativo.getId());
				st.setString(2, ativo.getNome());
				st.setDouble(3, ativo.getValorPorUnidade());

				st.executeUpdate();

				if (ativo instanceof Acao)
					ativo = DAOHelper.putAcao(c, ativo);

				if (ativo instanceof Moeda)
					ativo = DAOHelper.putMoeda(c, ativo);

				if (ativo instanceof Indice)
					ativo = DAOHelper.putIndice(c, ativo);

				if (ativo instanceof Commodity)
					ativo = DAOHelper.putCommodity(c, ativo);
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		Connect.close(c);

		return ativo;

	}

	@Override
	public Ativo remove(Object o) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ? extends Ativo> map) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Collection<Ativo> values() {

		Connection c = Connect.connect();
		if (c == null){
			return null;
		}

		AtivoTipoDAO a = new AtivoTipoDAO();

		List<Ativo> ativos = new ArrayList<>();

		for(int i = 0; i < AtivoConsts.TOTAL_TIPOS_ATIVOS; i++) {

			ativos.addAll(a.get(AtivoConsts.ALL_ATIVOS[i]));
		}

		Connect.close(c);

		return ativos;
	}

	@Override
	public Set<Entry<String, Ativo>> entrySet() {
		return null;
	}
}