<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Quantum Store</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }

        body {
            background: #f0f2f5;
            padding: 15px;
            min-height: 100vh;
        }

        .container {
            max-width: 100%;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.1);
            overflow: hidden;
        }

        header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px 20px;
            text-align: center;
        }

        header h1 {
            font-size: 2rem;
            margin-bottom: 8px;
            word-wrap: break-word;
        }

        .subtitle {
            font-size: 1rem;
            opacity: 0.9;
        }

        .content {
            display: grid;
            grid-template-columns: 1fr;
            gap: 25px;
            padding: 20px;
        }

        @media (min-width: 992px) {
            .content {
                grid-template-columns: 1fr 1fr;
            }
        }

        .section {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 20px;
            overflow: hidden;
        }

        .section h2 {
            color: #333;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
            margin-bottom: 15px;
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 1.3rem;
        }

        .badge {
            background: #667eea;
            color: white;
            padding: 4px 10px;
            border-radius: 20px;
            font-size: 0.85rem;
            white-space: nowrap;
        }

        /* Таблица - адаптивная */
        .table-container {
            width: 100%;
            overflow-x: auto;
            margin-top: 15px;
            -webkit-overflow-scrolling: touch;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 600px;
        }

        th {
            background: #667eea;
            color: white;
            padding: 10px 12px;
            text-align: left;
            font-size: 0.9rem;
            white-space: nowrap;
        }

        td {
            padding: 10px 12px;
            border-bottom: 1px solid #e0e0e0;
            vertical-align: top;
        }

        tr:hover {
            background: #f5f7ff;
        }

        .product-name {
            color: #333;
            font-weight: 500;
            word-break: break-word;
            min-width: 120px;
        }

        .price {
            color: #2e7d32;
            font-weight: bold;
            white-space: nowrap;
        }

        .stock {
            font-weight: 500;
            white-space: nowrap;
        }

        .in-stock {
            color: #2e7d32;
        }

        .low-stock {
            color: #f57c00;
        }

        .out-of-stock {
            color: #d32f2f;
        }

        .btn {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            padding: 7px 14px;
            border-radius: 20px;
            cursor: pointer;
            font-weight: 500;
            transition: transform 0.2s;
            font-size: 13px;
            white-space: nowrap;
        }

        .btn:hover {
            transform: translateY(-2px);
        }

        .btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .quantity-input {
            width: 55px;
            padding: 5px;
            border: 1px solid #ddd;
            border-radius: 4px;
            text-align: center;
            font-size: 13px;
        }

        .quantity-input:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 0 2px rgba(102, 126, 234, 0.2);
        }

        .qty-controls {
            display: flex;
            align-items: center;
            gap: 6px;
            flex-wrap: wrap;
        }

        .qty-btn {
            width: 26px;
            height: 26px;
            border-radius: 50%;
            border: none;
            background: #667eea;
            color: white;
            font-weight: bold;
            cursor: pointer;
            font-size: 14px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
        }

        .qty-btn:disabled {
            background: #ccc;
            cursor: not-allowed;
        }

        .qty-btn:hover:not(:disabled) {
            background: #5a6fd8;
        }

        .remove-btn {
            background: #ff5252;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.85rem;
            white-space: nowrap;
        }

        .remove-btn:hover {
            background: #ff3d3d;
        }

        .cart-total {
            margin-top: 20px;
            padding-top: 15px;
            border-top: 2px solid #667eea;
            text-align: right;
            font-size: 1.1rem;
            color: #333;
            overflow: hidden;
        }

        .total-amount {
            color: #2e7d32;
            font-size: 1.3rem;
            font-weight: bold;
            word-break: break-word;
        }

        .cart-actions {
            display: flex;
            flex-wrap: wrap;
            justify-content: space-between;
            margin-top: 20px;
            gap: 10px;
        }

        .clear-btn {
            background: #757575;
            color: white;
            border: none;
            padding: 8px 16px;
            border-radius: 20px;
            cursor: pointer;
            font-size: 0.9rem;
            flex: 1;
            min-width: 140px;
        }

        .clear-btn:hover {
            background: #616161;
        }

        .checkout-btn {
            background: linear-gradient(135deg, #ff416c 0%, #ff4b2b 100%);
            color: white;
            border: none;
            padding: 8px 20px;
            border-radius: 20px;
            cursor: pointer;
            font-weight: bold;
            font-size: 0.95rem;
            flex: 1;
            min-width: 140px;
        }

        .checkout-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(255, 65, 108, 0.3);
        }

        .messages {
            margin: 15px;
        }

        .alert {
            padding: 12px 15px;
            border-radius: 6px;
            margin-bottom: 12px;
            transition: opacity 0.5s;
            font-size: 0.9rem;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
            border-left: 4px solid #28a745;
        }

        .alert-error {
            background: #f8d7da;
            color: #721c24;
            border-left: 4px solid #dc3545;
        }

        .alert-info {
            background: #d1ecf1;
            color: #0c5460;
            border-left: 4px solid #17a2b8;
        }

        .empty-cart {
            text-align: center;
            padding: 30px 20px;
            color: #666;
        }

        .empty-cart-icon {
            font-size: 3rem;
            margin-bottom: 10px;
            opacity: 0.3;
        }

        .add-form {
            display: flex;
            flex-wrap: wrap;
            align-items: center;
            gap: 6px;
        }

        .add-quantity {
            width: 60px;
        }

        .stock-info {
            font-size: 11px;
            color: #666;
            margin-top: 3px;
            width: 100%;
        }

        @media (max-width: 768px) {
            body {
                padding: 10px;
            }

            header {
                padding: 20px 15px;
            }

            header h1 {
                font-size: 1.7rem;
            }

            .section {
                padding: 15px;
            }

            .section h2 {
                font-size: 1.2rem;
            }

            th, td {
                padding: 8px 10px;
                font-size: 0.85rem;
            }

            .quantity-input {
                width: 50px;
                padding: 4px;
            }

            .btn {
                padding: 6px 12px;
                font-size: 12px;
            }

            .cart-actions {
                flex-direction: column;
            }

            .clear-btn, .checkout-btn {
                width: 100%;
                text-align: center;
            }

            table {
                min-width: 500px;
            }
        }

        @media (max-width: 480px) {
            .add-form {
                flex-direction: column;
                align-items: flex-start;
            }

            .qty-controls {
                flex-direction: column;
                align-items: flex-start;
            }

            .product-name {
                min-width: 100px;
            }

            table {
                min-width: 400px;
            }
        }

        .price-cell {
            min-width: 100px;
        }

        .action-cell {
            min-width: 150px;
        }
    </style>
</head>
<body>
<div class="container">
    <header>
        <h1>Quantum Store</h1>
    </header>

    <div class="messages">
        <c:if test="${not empty param.error}">
            <div class="alert alert-error">
                ⚠️
                <c:choose>
                    <c:when test="${param.error == 'nostock'}">Товар закончился на складе</c:when>
                    <c:when test="${param.error == 'notfound'}">Товар не найден</c:when>
                    <c:when test="${param.error == 'limit'}">Достигнут лимит (макс. 10 шт. в корзине)</c:when>
                    <c:when test="${param.error == 'stock-error'}">Ошибка обновления запаса</c:when>
                    <c:when test="${param.error == 'no-id'}">Не указан ID товара</c:when>
                    <c:when test="${param.error == 'invalid'}">Неверный формат данных</c:when>
                    <c:otherwise>Произошла ошибка</c:otherwise>
                </c:choose>
            </div>
        </c:if>

        <c:if test="${not empty param.added}">
            <div class="alert alert-success">✅ Товар добавлен в корзину</div>
        </c:if>

        <c:if test="${not empty param.msg && param.msg == 'cleared'}">
            <div class="alert alert-info">🗑️ Корзина очищена</div>
        </c:if>

        <c:if test="${not empty param.msg && param.msg == 'order-success'}">
            <div class="alert alert-success">🎉 Заказ успешно оформлен! Спасибо за покупку!</div>
        </c:if>
    </div>

    <div class="content">
        <div class="section">
            <h2>📦 Каталог товаров
                <c:if test="${not empty catalog}">
                    <span class="badge">${catalog.size()}</span>
                </c:if>
            </h2>

            <div class="table-container">
                <table>
                    <thead>
                    <tr>
                        <th>Товар</th>
                        <th>Цена</th>
                        <th>Наличие</th>
                        <th class="action-cell">Действия</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="product" items="${catalog}">
                        <tr>
                            <td class="product-name">${product.name}</td>
                            <td class="price price-cell">${product.price} ₽</td>
                            <td>
                                <c:choose>
                                    <c:when test="${product.stock > 10}">
                                        <span class="stock in-stock">✓ ${product.stock} шт.</span>
                                    </c:when>
                                    <c:when test="${product.stock > 0}">
                                        <span class="stock low-stock">⚠️ ${product.stock} шт.</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="stock out-of-stock">✗ Нет в наличии</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="action-cell">
                                <c:if test="${product.stock > 0}">
                                    <form method="post" action="${pageContext.request.contextPath}/shop/add"
                                          class="add-form" onsubmit="return validateAddForm(${product.id}, ${product.stock})">
                                        <input type="hidden" name="id" value="${product.id}">

                                        <input type="number"
                                               name="quantity"
                                               class="quantity-input add-quantity"
                                               min="1"
                                               max="${product.stock > 10 ? 10 : product.stock}"
                                               value="1"
                                               onchange="validateQuantity(this, ${product.stock})">

                                        <button type="submit" class="btn">+ Добавить</button>
                                    </form>
                                    <div class="stock-info">
                                        Макс. можно добавить: ${product.stock > 10 ? 10 : product.stock} шт.
                                    </div>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="section">
            <h2>🛒 Корзина
                <c:if test="${not empty cartItems}">
                    <span class="badge">${cartItems.size()}</span>
                </c:if>
            </h2>

            <c:choose>
                <c:when test="${empty cartItems}">
                    <div class="empty-cart">
                        <div class="empty-cart-icon">🛒</div>
                        <p>Корзина пуста</p>
                        <p>Добавьте товары из каталога</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="table-container">
                        <table>
                            <thead>
                            <tr>
                                <th>Товар</th>
                                <th>Цена</th>
                                <th>Кол-во</th>
                                <th class="price-cell">Сумма</th>
                                <th>Действие</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <tr id="cart-row-${item.product.id}">
                                    <td class="product-name">${item.product.name}</td>
                                    <td class="price price-cell">${item.product.price} ₽</td>
                                    <td>
                                        <div class="qty-controls">
                                            <form method="post" action="${pageContext.request.contextPath}/shop/update-cart-quantity"
                                                  style="display: inline;">
                                                <input type="hidden" name="id" value="${item.product.id}">
                                                <input type="hidden" name="action" value="decrease">
                                                <button type="submit" class="qty-btn"
                                                        <c:if test="${item.quantity <= 1}">disabled</c:if>>-</button>
                                            </form>

                                            <form method="post" action="${pageContext.request.contextPath}/shop/update-cart-quantity"
                                                  class="quantity-form" style="display: inline;">
                                                <input type="hidden" name="id" value="${item.product.id}">
                                                <input type="number"
                                                       name="quantity"
                                                       class="quantity-input"
                                                       value="${item.quantity}"
                                                       min="1"
                                                       max="10"
                                                       onchange="updateCartQuantity(${item.product.id}, this.value)">
                                            </form>

                                            <form method="post" action="${pageContext.request.contextPath}/shop/update-cart-quantity"
                                                  style="display: inline;">
                                                <input type="hidden" name="id" value="${item.product.id}">
                                                <input type="hidden" name="action" value="increase">
                                                <button type="submit" class="qty-btn"
                                                        <c:if test="${item.quantity >= 10}">disabled</c:if>>+</button>
                                            </form>
                                        </div>
                                    </td>
                                    <td class="price price-cell">${item.totalPrice} ₽</td>
                                    <td>
                                        <form method="post" action="${pageContext.request.contextPath}/shop/update-cart"
                                              onsubmit="return confirm('Удалить товар из корзины?')">
                                            <input type="hidden" name="id" value="${item.product.id}">
                                            <input type="hidden" name="action" value="remove">
                                            <button type="submit" class="remove-btn">× Удалить</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>

                    <div class="cart-total">
                        <strong>Итого:</strong> <span class="total-amount">${totalCartPrice} ₽</span>
                    </div>

                    <div class="cart-actions">
                        <form method="post" action="${pageContext.request.contextPath}/shop/update-cart"
                              onsubmit="return confirm('Очистить всю корзину? Все товары вернутся на склад.')">
                            <input type="hidden" name="action" value="clear">
                            <button type="submit" class="clear-btn">🗑️ Очистить корзину</button>
                        </form>

                        <button class="checkout-btn" onclick="checkout('${totalCartPrice}')">💳 Оформить заказ</button>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<script>
    function validateAddForm(productId, maxStock) {
        const form = document.querySelector(`form[action$="/shop/add"] input[name="id"][value="${productId}"]`).closest('form');
        const quantityInput = form.querySelector('input[name="quantity"]');
        let quantity = parseInt(quantityInput.value);

        if (isNaN(quantity) || quantity < 1) {
            alert('Количество должно быть не менее 1');
            quantityInput.value = 1;
            return false;
        }

        const maxCanAdd = Math.min(10, maxStock);
        if (quantity > maxCanAdd) {
            alert(`Максимальное количество для добавления: ${maxCanAdd}`);
            quantityInput.value = maxCanAdd;
            return false;
        }

        return true;
    }

    function validateQuantity(input, maxStock) {
        let value = parseInt(input.value);

        if (isNaN(value) || value < 1) {
            input.value = 1;
            return;
        }

        const maxCanAdd = Math.min(10, maxStock);
        if (value > maxCanAdd) {
            input.value = maxCanAdd;
            alert(`Максимальное количество: ${maxCanAdd}`);
        }
    }

    function updateCartQuantity(productId, quantity) {
        quantity = parseInt(quantity);

        if (isNaN(quantity) || quantity < 1) {
            alert('Количество должно быть не менее 1');
            location.reload();
            return;
        }

        if (quantity > 10) {
            alert('Максимальное количество: 10');
            quantity = 10;
        }

        const form = document.querySelector(`#cart-row-${productId} .quantity-form`);
        if (form) {
            const quantityInput = form.querySelector('input[name="quantity"]');
            quantityInput.value = quantity;
            form.submit();
        }
    }

    function checkout(totalPrice) {
        if (confirm(`Оформить заказ на сумму ${totalPrice} ₽?`)) {
            alert('Заказ успешно оформлен! Спасибо за покупку!');
            window.location.href = '${pageContext.request.contextPath}/shop?msg=order-success';
        }
    }

    document.addEventListener('DOMContentLoaded', function() {
        setTimeout(() => {
            document.querySelectorAll('.alert').forEach(alert => {
                alert.style.opacity = '0';
                setTimeout(() => {
                    if (alert.parentNode) {
                        alert.parentNode.removeChild(alert);
                    }
                }, 500);
            });
        }, 5000);

        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('keypress', function(e) {
                if (e.key === 'Enter') {
                    e.preventDefault();
                    if (this.closest('.add-form')) {
                        this.closest('form').submit();
                    } else if (this.closest('.quantity-form')) {
                        const productId = this.closest('form').querySelector('input[name="id"]').value;
                        updateCartQuantity(productId, this.value);
                    }
                }
            });
        });

        function adjustTables() {
            const tables = document.querySelectorAll('.table-container');
            tables.forEach(container => {
                const table = container.querySelector('table');
                if (table.offsetWidth > container.offsetWidth) {
                    container.style.overflowX = 'scroll';
                } else {
                    container.style.overflowX = 'auto';
                }
            });
        }

        adjustTables();
        window.addEventListener('resize', adjustTables);
    });
</script>
</body>
</html>