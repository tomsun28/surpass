// 处理主题样式
export function handleThemeStyle(theme : any): any {
	document.documentElement.style.setProperty('--el-color-primary', theme)
	for (let i: any = 1; i <= 9; i++) {
		document.documentElement.style.setProperty(`--el-color-primary-light-${i}`, `${getLightColor(theme, i / 10)}`)
	}
	for (let i: any = 1; i <= 9; i++) {
		document.documentElement.style.setProperty(`--el-color-primary-dark-${i}`, `${getDarkColor(theme, i / 10)}`)
	}
}

// hex颜色转rgb颜色
export function hexToRgb(str : any): any {
	str = str.replace('#', '')
	let hexs: any = str.match(/../g)
	for (let i: any = 0; i < 3; i++) {
		hexs[i] = parseInt(hexs[i], 16)
	}
	return hexs
}

// rgb颜色转Hex颜色
export function rgbToHex(r : any, g : any, b : any): any {
	let hexs: any = [r.toString(16), g.toString(16), b.toString(16)]
	for (let i: any = 0; i < 3; i++) {
		if (hexs[i].length == 1) {
			hexs[i] = `0${hexs[i]}`
		}
	}
	return `#${hexs.join('')}`
}

// 变浅颜色值
export function getLightColor(color : any, level : any): any {
	let rgb: any = hexToRgb(color)
	for (let i: any = 0; i < 3; i++) {
		rgb[i] = Math.floor((255 - rgb[i]) * level + rgb[i])
	}
	return rgbToHex(rgb[0], rgb[1], rgb[2])
}

// 变深颜色值
export function getDarkColor(color : any, level : any): any {
	let rgb: any = hexToRgb(color)
	for (let i: any = 0; i < 3; i++) {
		rgb[i] = Math.floor(rgb[i] * (1 - level))
	}
	return rgbToHex(rgb[0], rgb[1], rgb[2])
}
