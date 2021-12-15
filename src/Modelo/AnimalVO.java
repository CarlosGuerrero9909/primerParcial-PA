package Modelo;

/**
 *
 * @author Carlos Guerrero
 * @author Nicolas DÃ­az
 */
public class AnimalVO {

    private String filum;
    private String subfilum;
    private String clase;
    private String orden;
    private String familia;
    private String genero;
    private String especie;
    private String nombre;
    private String imagen;
    private String sonido;

    /**
     * Constructor 1
     */
    public AnimalVO() {

    }

    /**
     * Constructor 2 el cual requiere un parametro para cada atributo que posee
     * la clase
     *
     * @param filum
     * @param subfilum
     * @param clase
     * @param orden
     * @param familia
     * @param genero
     * @param especie
     * @param nombre
     * @param imagen
     * @param sonido
     */
    public AnimalVO(String filum, String subfilum, String clase, String orden, String familia, String genero, String especie, String nombre, String imagen, String sonido) {
        this.filum = filum;
        this.subfilum = subfilum;
        this.clase = clase;
        this.orden = orden;
        this.familia = familia;
        this.genero = genero;
        this.especie = especie;
        this.nombre = nombre;
        this.imagen = imagen;
        this.sonido = sonido;

    }

    // Setters y getters
    public String getFilum() {
        return filum;
    }

    public void setFilum(String filum) {
        this.filum = filum;
    }

    public String getSubfilum() {
        return subfilum;
    }

    public void setSubfilum(String subfilum) {
        this.subfilum = subfilum;
    }

    public String getClase() {
        return clase;
    }

    public void setClase(String clase) {
        this.clase = clase;
    }

    public String getOrden() {
        return orden;
    }

    public void setOrden(String orden) {
        this.orden = orden;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getSonido() {
        return sonido;
    }

    public void setSonido(String sonido) {
        this.sonido = sonido;
    }

}