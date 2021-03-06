package control;

import dao.DemDao;
import dao.DemJDBCDao;
import dao.TipoEntradaDao;
import dao.TipoEntradaJDBCDao;
import dao.LoteDao;
import dao.LoteJDBCDao;
import domain.Entrada;
import domain.Fornecedor;
import domain.Lote;
import domain.Material;
import domain.TipoDocumento;
import domain.TipoEntrada;
import domain.Usuario;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class ControladoraEntradaMaterial {

    private Vector<TipoEntrada> tipoEntradas;
    private Vector<Lote> lotes;
    private Vector tiposEntrada;
    private TipoEntradaDao tipoEntradaDao;
    private TipoEntrada tipoEntrada;
    private TipoDocumento tipoDoc;
    private Usuario usuario;
    private Fornecedor fornecedor;
    private Entrada entrada;
    private DemDao demDao;
    private LoteDao loteDao;

    public ControladoraEntradaMaterial(String servidor) {
        this.tipoEntradaDao = new TipoEntradaJDBCDao(servidor);
        this.loteDao = new LoteJDBCDao(servidor);
        this.demDao = new DemJDBCDao(servidor);
        this.lotes = new Vector<Lote>();
        this.entrada = new Entrada();

    }

    private void attEntrada(Vector entrada) {
        this.tipoEntrada = new TipoEntrada();
        this.tipoEntrada.setCodigo(Integer.parseInt(entrada.get(0).toString()));

        this.tipoDoc = new TipoDocumento();
        this.tipoDoc.setSiglaTipoDoc(entrada.get(7).toString());

        this.fornecedor = new Fornecedor();
        this.fornecedor.setId(entrada.get(1).toString());

        this.usuario = new Usuario();
        this.usuario.setCodigo(Integer.parseInt(entrada.get(6).toString()));

        this.tipoEntrada.setTipoDoc(this.tipoDoc);
        this.entrada.setReponsavel(this.usuario);
        this.entrada.setTipoEntrada(this.tipoEntrada);
        this.entrada.setFornecedor(this.fornecedor);
        this.entrada.setTipoEntrada(this.tipoEntrada);

        this.entrada.setNumNota(entrada.get(2).toString());
        this.entrada.setDataNota(new Date(entrada.get(3).toString()));
        this.entrada.setValorNota(Double.parseDouble(entrada.get(4).toString().replace(",", ".")));
        this.entrada.setNumNE(entrada.get(5).toString());
    }

    public int inserirEntrada(Vector entrada, Vector lote, Vector documento) throws SQLException {
        this.attEntrada(entrada);
        this.attLote(lote);

        this.demDao.inserirDem(this.entrada, this.lotes);
        return this.demDao.obterCodigoUltimo().getCodigo();
    }

    private void attLote(Vector materiais) {
        Vector materialAtual = new Vector();
        Lote loteAtual;
        for (int i = 0; i < materiais.size(); i++) {
            materialAtual = (Vector) materiais.get(i);

            Material material = new Material();
            material.setCodigo(Integer.parseInt(materialAtual.get(0).toString()));
            material.setEstoqueAtual(Double.parseDouble(materialAtual.get(3).toString()));

            loteAtual = new Lote();
            loteAtual.setMaterial(material);
            loteAtual.setQuantidadeTotal(Double.parseDouble(materialAtual.get(3).toString()));
            loteAtual.setEstoque(Double.parseDouble(materialAtual.get(3).toString()));
            loteAtual.setValidade(new Date(materialAtual.get(4).toString()));
            loteAtual.setValorUnidade(Double.parseDouble(materialAtual.get(5).toString()));
            this.lotes.add(loteAtual);
        }
    }

    private void obterTipoEntrada() throws SQLException {
        try {
            this.tipoEntradas = this.tipoEntradaDao.obterTipoEntrada();
        } catch (SQLException ex) {
            throw new SQLException();
        }
    }

    private Vector criarLinhaDem(Vector<Entrada> dem) {
        Vector demFinal = new Vector();
        for (int i = 0; i < dem.size(); i++) {
            Entrada demAtual = new Entrada();
            Vector linha = new Vector();
            demAtual = dem.get(i);
            linha.add(demAtual.getCodigo());
            linha.add(demAtual.getDocumento().getId_documento());
            linha.add(demAtual.getNumNE());
            linha.add(demAtual.getFornecedor().getNome());
            linha.add(demAtual.getTipoEntrada().getNome());
            linha.add(demAtual.getDocumento().getDataDoc());
            linha.add(demAtual.getValorNota());
            demFinal.add(linha);
        }
        return demFinal;
    }

    private Vector criarLinhaLote(Vector<Lote> lotes) {
        Vector loteFinal = new Vector();
        for (int i = 0; i < lotes.size(); i++) {
            Vector linha = new Vector();
            Lote loteAtual = lotes.get(i);
            linha.add(loteAtual.getCodigo());
            linha.add(loteAtual.getMaterial().getDescricao());
            linha.add(loteAtual.getQuantidadeTotal());
            linha.add(loteAtual.getValidade());
            linha.add(loteAtual.getValorUnidade());
            loteFinal.add(linha);
        }
        return loteFinal;
    }

    public Vector obterLotesPor(int cod) throws SQLException {
        Entrada dem = new Entrada();
        dem.setCodigo(cod);
        return criarLinhaLote(this.loteDao.obterLotesDem(dem));
    }

    public Vector obterDemEntre(Date dataInicial, Date dataFinal) throws SQLException {
        Vector<Entrada> dems = this.demDao.obterTodosDemEntre(dataInicial, dataFinal);
        return criarLinhaDem(dems);
    }

    public Vector ListarTodosTipoEntrada() throws SQLException {
        this.obterTipoEntrada();
        Vector linhas = new Vector();
        for (int i = 0; i < this.tipoEntradas.size(); i++) {
            TipoEntrada te = this.tipoEntradas.get(i);
            linhas.addElement(this.criarLinhaTipoEntrada(te));
        }
        return linhas;
    }

    private Vector criarLinhaTipoEntrada(TipoEntrada tipo) {
        Vector linha = new Vector();
        linha.add(tipo.getCodigo());
        linha.add(tipo.getNome());
        linha.add(tipo.getTipoDoc().getNomeTipoDoc());
        linha.add(tipo.getTipoDoc().getSiglaTipoDoc());
        return linha;
    }

    public Vector<TipoEntrada> getTipoEntrada() {
        return tipoEntradas;
    }

    public void setTipoEntrada(Vector<TipoEntrada> tipoEntrada) {
        this.tipoEntradas = tipoEntrada;
    }

    public TipoEntradaDao getTipoEntradaDao() {
        return tipoEntradaDao;
    }

    public void setTipoEntradaDao(TipoEntradaDao tipoEntradaDao) {
        this.tipoEntradaDao = tipoEntradaDao;
    }

    public Vector getTiposEntrada() {
        return tiposEntrada;
    }

    public void setTiposEntrada(Vector tiposEntrada) {
        this.tiposEntrada = tiposEntrada;
    }

    public DemDao getDemDao() {
        return demDao;
    }

    public void setDemDao(DemDao demDao) {
        this.demDao = demDao;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public LoteDao getLoteDao() {
        return loteDao;
    }

    public void setLoteDao(LoteDao loteDao) {
        this.loteDao = loteDao;
    }

    public Vector<Lote> getLotes() {
        return lotes;
    }

    public void setLotes(Vector<Lote> lotes) {
        this.lotes = lotes;
    }

    public TipoDocumento getTipoDoc() {
        return tipoDoc;
    }

    public void setTipoDoc(TipoDocumento tipoDoc) {
        this.tipoDoc = tipoDoc;
    }

    public void setTipoEntrada(TipoEntrada tipoEntrada) {
        this.tipoEntrada = tipoEntrada;
    }

    public Vector<TipoEntrada> getTipoEntradas() {
        return tipoEntradas;
    }

    public void setTipoEntradas(Vector<TipoEntrada> tipoEntradas) {
        this.tipoEntradas = tipoEntradas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
