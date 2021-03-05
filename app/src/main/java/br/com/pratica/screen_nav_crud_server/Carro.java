package br.com.pratica.screen_nav_crud_server;

public class Carro {
    private int id;
    private String placa;
    private String nome;
    private int ano;

    public Carro(int id, String placa, String nome, int ano) {
        this.id = id + 1;
        this.placa = placa;
        this.nome = nome;
        this.ano = ano;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }
}
