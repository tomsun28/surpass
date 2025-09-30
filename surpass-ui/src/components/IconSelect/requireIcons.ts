let icons: any = []
const modules: any = import.meta.glob('./../../assets/icons/svg/**/*.svg');
for (const path in modules) {
  const rawName = path.split('assets/icons/svg/')[1].split('.svg')[0];
  const iconName = rawName.replace(/\//g, '-'); // 将子目录中的 `/` 替换为 `-`
  icons.push(iconName);
}

export default icons;
