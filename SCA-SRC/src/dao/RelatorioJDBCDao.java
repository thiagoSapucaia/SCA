package dao;

import domain.Entrada;
import domain.Saida;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

public class RelatorioJDBCDao implements RelatorioDao {

    private Connection conexao;
    private String servidor;

    public RelatorioJDBCDao(String servidor) {
        this.servidor = servidor;
    }

    private void gerarRelatorio(HashMap parametro, String arquivo) throws JRException {
        JasperPrint jp = JasperFillManager.fillReport(arquivo, parametro, conexao);
        JasperViewer.viewReport(jp, false);
    }

    private String getLocal() {
        return System.getProperty("user.dir") + "/SCA/report/";
    }

    public void getBoletim(Date dataInicio, Date dataFim) throws SQLException, JRException {
        Date dataInicioAno = new Date(dataInicio.getYear(), 0, 1);
        try {
            conexao = FabricaConexao.obterConexao("JDBC", this.servidor);
            String arquivo = getLocal();
            arquivo += "Boletim/RptBoletim.jasper";
            HashMap parametro = new HashMap();
            parametro.put("dataInicio", dataInicio);
            parametro.put("dataFim", dataFim);
            parametro.put("dataInicioAno", dataInicioAno);
            gerarRelatorio(parametro, arquivo);
            conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }

    public void getRMM(Date dataInicio, Date dataFim) throws SQLException, JRException {
        Date dataInicioAno = new Date(dataInicio.getYear(), 0, 1);
        Date dataFimAnoAnterior = new Date(dataInicio.getYear() - 1, 11, 31);
        Date dataInicioMes = new Date(dataInicio.getYear(), dataInicio.getMonth() - 1, 1);
        Date dataInicioSistema = new Date("01/01/1970");
        try {
            conexao = FabricaConexao.obterConexao("JDBC", this.servidor);
            String arquivo = getLocal();
            arquivo += "RMM/RptRMM.jasper";
            HashMap parametro = new HashMap();
            parametro.put("dataInicio", dataInicio);
            parametro.put("dataFim", dataFim);
            parametro.put("dataInicioAno", dataInicioAno);
            parametro.put("dataFimAnoAnterior", dataFimAnoAnterior);
            parametro.put("dataInicioMes", dataInicioMes);
            parametro.put("dataInicioSistema", dataInicioSistema);
            Date d = new Date(dataInicioMes.getYear(), dataInicioMes.getMonth() + 1, dataInicioMes.getDate() - 1);
            parametro.put("dataFimMesAnterior", d);
            gerarRelatorio(parametro, arquivo);
            conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }

    public void getDEM(Entrada dem) throws SQLException, JRException {
        try {
            this.conexao = FabricaConexao.obterConexao("JDBC", this.servidor);
            String arquivo = getLocal();
            arquivo += "DEM/DEM.jasper";
            HashMap parametro = new HashMap();
            parametro.put("codigo_dem", dem.getCodigo());
            gerarRelatorio(parametro, arquivo);
            this.conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }

    public void getRCM(Date dataI, Date dataF, int codigoSetor) throws SQLException, JRException {
        try {
            conexao = FabricaConexao.obterConexao("JDBC", this.servidor);
            String arquivo = getLocal();
            arquivo += "Consumo/RptRCM.jasper";
            HashMap parametro = new HashMap();
            parametro.put("dataInicio", dataI);
            parametro.put("dataFim", dataF);
            parametro.put("codSetor", codigoSetor);
            gerarRelatorio(parametro, arquivo);
            conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }

    public void getRME() throws SQLException, JRException {
        try {
            conexao = FabricaConexao.obterConexao("JDBC", this.servidor);
            String arquivo = getLocal();
            arquivo += "Estoque/RptEstoque.jasper";
            gerarRelatorio(new HashMap(), arquivo);
            conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }

    public void getLog(String classe) throws SQLException, JRException {
        try {
            conexao = FabricaConexao.obterConexao("JDBC", this.servidor);
            String arquivo = getLocal();
            arquivo += "Logs/RptLOG" + classe + ".jasper";
            gerarRelatorio(new HashMap(), arquivo);
            conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }

    public void getDsm(Saida saida) throws SQLException, JRException {
        try {
            this.conexao = FabricaConexao.obterConexao("JDBC", "localhost");
            String arquivo = getLocal();
            arquivo += "DSM/DSM.jasper";
            HashMap parametro = new HashMap();
            parametro.put("codigo_dsm", saida.getCodigo());
            gerarRelatorio(parametro, arquivo);
            this.conexao.close();
        } catch (JRException ex) {
            throw new JRException(ex.getCause());
        } catch (SQLException ex) {
            throw new SQLException(ex.getCause());
        }
    }
}
