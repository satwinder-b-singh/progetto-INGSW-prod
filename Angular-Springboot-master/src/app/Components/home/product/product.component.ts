import { Component, OnInit, Input, Output } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Product } from 'src/app/Model/product';
import { EventEmitter } from '@angular/core';



@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {

  @Input() public product;

  @Output() productAddToCart: EventEmitter<Product> = new EventEmitter<Product>();
  constructor(private http: HttpClient) { }
  @Output() showProduct: EventEmitter<Product> = new EventEmitter<Product>();


  ngOnInit() {

  }

  addToCart() {
    this.productAddToCart.emit(this.product);
  }
 show(id)
 {
  this.showProduct.emit(this.product);
 }
}
