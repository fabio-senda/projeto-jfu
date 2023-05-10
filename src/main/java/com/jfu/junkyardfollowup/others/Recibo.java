package com.jfu.junkyardfollowup.others;

import com.jfu.junkyardfollowup.models.Fornecimento;
import com.jfu.junkyardfollowup.models.RegistroDeCompra;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class Recibo{
    private RegistroDeCompra compra;
    private Document documentoPDF;
    private List<Fornecimento> fornecimentoList;
    private BigDecimal total;

    public Recibo(RegistroDeCompra compra, List<Fornecimento> fornecimentoList, BigDecimal total) {
        this.compra = compra;
        this.documentoPDF = new Document();
        this.fornecimentoList = fornecimentoList;
        this.total = total;
    }

    public void gerarCabecalho() {
        Paragraph paragrafoTitulo = new Paragraph();
        paragrafoTitulo.setAlignment(Element.ALIGN_CENTER);
        paragrafoTitulo.add(new Chunk("RECIBO DE VENDA", new Font(Font.HELVETICA, 24)));
        documentoPDF.add(paragrafoTitulo);

        this.documentoPDF.add(new Paragraph(" "));

        Paragraph paragrafoData = new Paragraph();
        paragrafoData.setAlignment(Element.ALIGN_CENTER);
        paragrafoData.add(new Chunk(this.compra.getData().toString()));
        this.documentoPDF.add(paragrafoData);
        this.documentoPDF.add(new Paragraph(" "));
        this.documentoPDF.add(new Paragraph(" "));

        Paragraph paragrafoCliente = new Paragraph();
        paragrafoCliente.setAlignment(Element.ALIGN_CENTER);
        paragrafoCliente.add(new Chunk("Fornecedor: " + this.compra.getFornecedor().getNome(), new Font(Font.BOLD, 16)));
        this.documentoPDF.add(paragrafoCliente);

        Paragraph paragrafoSessao = new Paragraph("----------------------------------------------------------");
        paragrafoSessao.setAlignment(Element.ALIGN_CENTER);
        this.documentoPDF.add(paragrafoSessao);
        this.documentoPDF.add(new Paragraph(" "));
    }

    public void gerarCorpo() {
        Paragraph pItensVendidos = new Paragraph();
        pItensVendidos.setAlignment(Element.ALIGN_CENTER);
        pItensVendidos.add(new Chunk("ITENS COMPRADOS", new Font(Font.TIMES_ROMAN, 16)));
        documentoPDF.add(new Paragraph(pItensVendidos));

        for (Fornecimento produto : fornecimentoList) {
            Paragraph pNomeProduto = new Paragraph();
            pNomeProduto.add(new Chunk(produto.getMaterial().getNome(), new Font(Font.COURIER, 14)));
            Paragraph pDadosProduto = new Paragraph();
            pDadosProduto.add("Peso:  " + produto.getQuantidade() + " kg  -  Preço por kg: R$ " + produto.getPreco()
                    + "  -  Total: R$ " + produto.getTotal());

            this.documentoPDF.add(pNomeProduto);
            this.documentoPDF.add(pDadosProduto);
            this.documentoPDF.add(new Paragraph("---------------"));

        }
        Paragraph pTotal = new Paragraph();
        pTotal.setAlignment(Element.ALIGN_RIGHT);
        pTotal.add(new Chunk("Total da compra: R$ " + total,
                new Font(Font.TIMES_ROMAN, 20)));
        this.documentoPDF.add(pTotal);
    }

    public void gerarRodape() {
        Paragraph paragrafoSessao = new Paragraph("----------------------------------------------------------");
        paragrafoSessao.setAlignment(Element.ALIGN_CENTER);
        this.documentoPDF.add(paragrafoSessao);
        this.documentoPDF.add(new Paragraph(" "));
        Paragraph pRodape = new Paragraph();
        pRodape.setAlignment(Element.ALIGN_CENTER);
        pRodape.add(new Chunk("Reciclagem 650", new Font(Font.TIMES_ROMAN, 14)));
        this.documentoPDF.add(pRodape);
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        this.documentoPDF = new Document();
        PdfWriter.getInstance(documentoPDF, response.getOutputStream());
        documentoPDF.open();
        gerarCabecalho();
        gerarCorpo();
        gerarRodape();
        documentoPDF.close();
    }
}