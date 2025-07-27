import api from './api';

export interface Product {
  id: number;
  name: string;
  description: string;
  price: number;
  category: string;
  stockQuantity: number;
}

export interface CreateProductRequest {
  name: string;
  description: string;
  price: number;
  category: string;
  stockQuantity: number;
}

export interface UpdateProductRequest {
  name?: string;
  description?: string;
  price?: number;
  category?: string;
  stockQuantity?: number;
}

export const productService = {
  async getAllProducts(): Promise<Product[]> {
    const response = await api.get('/products');
    return response.data as Product[];
  },

  async getProductById(id: number): Promise<Product> {
    const response = await api.get(`/products/${id}`);
    return response.data as Product;
  },

  async createProduct(productData: CreateProductRequest): Promise<Product> {
    const response = await api.post('/products', productData);
    return response.data as Product;
  },

  async updateProduct(id: number, productData: UpdateProductRequest): Promise<Product> {
    const response = await api.put(`/products/${id}`, productData);
    return response.data as Product;
  },

  async deleteProduct(id: number): Promise<void> {
    await api.delete(`/products/${id}`);
  }
}; 