package Modelo;

/**
 * Esta clase crea objetos tipo animal
 *
 * @author Carlos Guerrero
 * @author Nicolas Di­az
 */
public class AnimalVO {

    // atributo emplados para cada animal que haga las veces de value object
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
     * Constructor 1 el cual permite crear objetos AnimalVO sin necesidad de variables de entrada
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

    /**
     *
     * @return
     */
    public String getFilum() {
        return filum;
    }

    /**
     *
     * @param filum
     */
    public void setFilum(String filum) {
        this.filum = filum;
    }

    /**
     *
     * @return
     */
    public String getSubfilum() {
        return subfilum;
    }

    /**
     *
     * @param subfilum
     */
    public void setSubfilum(String subfilum) {
        this.subfilum = subfilum;
    }

    /**
     *
     * @return
     */
    public String getClase() {
        return clase;
    }

    /**
     *
     * @param clase
     */
    public void setClase(String clase) {
        this.clase = clase;
    }

    /**
     *
     * @return
     */
    public String getOrden() {
        return orden;
    }

    /**
     *
     * @param orden
     */
    public void setOrden(String orden) {
        this.orden = orden;
    }

    /**
     *
     * @return
     */
    public String getFamilia() {
        return familia;
    }

    /**
     *
     * @param familia
     */
    public void setFamilia(String familia) {
        this.familia = familia;
    }

    /**
     *
     * @return
     */
    public String getGenero() {
        return genero;
    }

    /**
     *
     * @param genero
     */
    public void setGenero(String genero) {
        this.genero = genero;
    }

    /**
     *
     * @return
     */
    public String getEspecie() {
        return especie;
    }

    /**
     *
     * @param especie
     */
    public void setEspecie(String especie) {
        this.especie = especie;
    }

    /**
     *
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     *
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     *
     * @return
     */
    public String getImagen() {
        return imagen;
    }

    /**
     *
     * @param imagen
     */
    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    /**
     *
     * @return
     */
    public String getSonido() {
        return sonido;
    }

    /**
     *
     * @param sonido
     */
    public void setSonido(String sonido) {
        this.sonido = sonido;
    }

}
