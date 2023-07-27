package demo.com.projects.website.model;

public class Product {
	private String id;
	private String categories;
	private String name;
	private String image;
	private Boolean inStock;
	private int price;

	public Product(String id, String categories, String name, String image, Boolean inStock, int price) {
		this.id = id;
		this.categories = categories;
		this.name = name;
		this.image = image;
		this.inStock = inStock;
		this.price = price;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCategories() {
		return categories;
	}

	public void setCategories(String categories) {
		this.categories = categories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Boolean getInStock() {
		return inStock;
	}

	public void setInStock(Boolean inStock) {
		this.inStock = inStock;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Product{" +
				"id='" + id + '\'' +
				", categories='" + categories + '\'' +
				", name='" + name + '\'' +
				", image='" + image + '\'' +
				", inStock=" + inStock +
				", price=" + price +
				'}';
	}
}
